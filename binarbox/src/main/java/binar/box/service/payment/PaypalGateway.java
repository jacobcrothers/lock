package binar.box.service.payment;

import binar.box.util.Exceptions.PaymentException;
import com.braintreegateway.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaypalGateway {
    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(PaypalService.class);

    /**
     * Reference to {@link BraintreeGateway}
     */
    @Autowired
    private BraintreeGateway braintreeGateway;

    /**
     * Generates and returns a client token that includes configuration and
     * authorization information required by the client application when making
     * requests with the PayPal client SDK.
     */
    public String generatePayPalClientToken() {
        return braintreeGateway.clientToken().generate();
    }

    /**
     * Creates a PayPal sale transaction that is automatically submitted for
     * settlement and returns the transaction's ID
     *
     * @param paymentMethodNonce
     *            a reference to the customer payment method details that were
     *            provided for this transaction
     * @param amount
     *            the amount of the transaction
     * @return the ID of the created transaction
     */
    public String createTransaction(final String paymentMethodNonce, final BigDecimal amount) {
        final TransactionRequest request = new TransactionRequest().amount(amount)
                .paymentMethodNonce(paymentMethodNonce).options().submitForSettlement(true).done();

        final Result<Transaction> result = braintreeGateway.transaction().sale(request);

        if (!result.isSuccess()) {
            // transaction failed
            if (result.getTransaction() != null) {
                // if the transaction failed because of a Gateway Rejection that
                // occurred because of the gateway settings of the Braintree
                // account
                LOGGER.error(String.format(
                        "PayPal transaction failed because of a Gateway Rejection. Gateway Rejection reason status: %s, message: %s",
                        result.getTransaction().getGatewayRejectionReason(), result.getMessage()));
            } else {
                // if the transaction failed because it was declined by the
                // customer's bank
                LOGGER.error(String.format(
                        "PayPal transaction failed because it was declined by the customer's bank, message: %s",
                        result.getMessage()));
                for (final ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                    LOGGER.error(
                            "PayPal transaction error: Code: " + error.getCode() + ", Message: " + error.getMessage());
                }
            }
            throw new PaymentException("Paypal transaction error at create","paypal.create.transaction.error");
        }
        // transaction was successful
        LOGGER.info(String.format("PayPal transaction %s was successful", result.getTarget().getId()));

        // returning the ID of the created transaction
        return result.getTarget().getId();
    }

    /**
     * Returns the funds to the customer's account. If the PayPal transaction
     * has the status Settling or Settled then it is refunded, if it has the
     * status Submitted for Settlement then it is voided.
     *
     * @param transactionID
     *            the ID of the PayPal transaction
     */
    public void refundTransaction(final String transactionID) {
        final Transaction transaction = braintreeGateway.transaction().find(transactionID);

        if (transaction.getStatus() == Transaction.Status.SUBMITTED_FOR_SETTLEMENT) {
            // transactions that have the status SUBMITTED_FOR_SETTLEMENT must
            // be voided, they can't be refunded

            voiding(transactionID);
        } else if (transaction.getStatus() == Transaction.Status.SETTLING || transaction.getStatus() == Transaction.Status.SETTLED) {
            // transactions that have the status SETTLING or SETTLED can be
            // refunded

            refund(transactionID);
        } else {
            throw new PaymentException(
                    String.format("The status %s of the Paypal transaction %s is invalid for refund or void operations", transaction.getStatus(), transactionID),
                    "transaction.status.invalid.refund.void",
                    transaction.getStatus(),
                    transactionID);
        }
    }

    /**
     * Voiding the PayPal transaction
     *
     * @param transactionID
     *            the ID of the PayPal transaction
     */
    private void voiding(final String transactionID) {
        final Result<Transaction> result = braintreeGateway.transaction().voidTransaction(transactionID);

        if (!result.isSuccess()) {
            // voiding failed

            // trying to refund the transaction if the status changed while
            // trying to void it
            final Transaction transaction = braintreeGateway.transaction().find(transactionID);
            if (transaction.getStatus() == Transaction.Status.SETTLING || transaction.getStatus() == Transaction.Status.SETTLED) {
                // if the status of the transaction changed from
                // SUBMITTED_FOR_SETTLEMENT to SETTLING or SETTLED while trying
                // to void the transaction
                refund(transactionID);
                return;
            }

            LOGGER.error(String.format("PayPal voiding of the transaction %s failed: %s", transactionID,
                    result.getMessage()));
            for (final ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                LOGGER.error(String.format("PayPal voiding of the transaction %s, error: Code: %s, Message: %s",
                        transactionID, error.getCode(), error.getMessage()));
            }

            throw new PaymentException(
                    String.format("PayPal voiding of the transaction %s failed: %s", transactionID, result.getMessage()),
                    "paypal.voiding.failed",
                    transactionID,
                    result.getMessage()
            );
        }
    }

    /**
     * Refunding the PayPal transaction
     *
     * @param transactionID
     *            the ID of the PayPal transaction
     */
    private void refund(final String transactionID) {
        final Result<Transaction> result = braintreeGateway.transaction().refund(transactionID);

        if (!result.isSuccess()) {
            // refunding failed
            LOGGER.error(String.format("PayPal refunding of the transaction %s failed: %s", transactionID,
                    result.getMessage()));
            for (final ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                LOGGER.error(String.format("PayPal refunding of the transaction %s, error: Code: %s, Message: %s",
                        transactionID, error.getCode(), error.getMessage()));
            }

            throw new PaymentException(
                    String.format("PayPal refunding of the transaction %s failed: %s", transactionID, result.getMessage()),
                    "paypal.refund.failed",
                    transactionID,
                    result.getMessage()
            );
        }
    }
}
