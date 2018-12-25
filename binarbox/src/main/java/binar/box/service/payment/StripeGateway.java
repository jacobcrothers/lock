package binar.box.service.payment;

import binar.box.util.Exceptions.PaymentException;
import com.stripe.Stripe;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class StripeGateway {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    //TODO remove  e.printStackTrace()  in production and replace it with a log
    public Customer createCustomer(String stripeToken, String description, String customerEmail) {
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("source", stripeToken);
        customerParams.put("email", customerEmail);
        customerParams.put("description", description);
        try {
            return Customer.create(customerParams);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on creating customer for email " + customerEmail);
        }
    }

    public ExternalAccountCollection getAllCards(String customerId) {
        Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put("object", "card");
        try {
            return Customer.retrieve(customerId).getSources().list(cardParams);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on retrieving all cards for customerId " + customerId);
        }
    }

    //this will charge from the default customer card
    public Charge charge(String customerId, int amount, String currency, boolean capture, String description) {
        return chargeCard(customerId, null, amount, currency, capture, description);
    }


    public void deleteCard(Customer customer, String cardId) {
        try {
            customer.getSources().retrieve(cardId).delete();
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on deleting card from customerId " + customer.getId());
        }
    }

    public Charge chargeCard(String customerId, String cardId, int amount, String currency, boolean capture, String description) {
        Map<String, Object> chargeParams = new HashMap<>();
        if (Objects.nonNull(cardId))
            chargeParams.put("source", cardId);

        chargeParams.put("customer", customerId);
        chargeParams.put("amount", amount);
        chargeParams.put("currency", currency);
        chargeParams.put("capture", capture);
        chargeParams.put("description", description);
        try {
            return Charge.create(chargeParams);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on charging card for customerId " + customerId);
        }
    }

    public Customer retrieveCustomer(String customerId) {
        try {
            return Customer.retrieve(customerId);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on retrieving customer for customerId " + customerId);
        }
    }

    public Token retrieveToken(String stripeToken) {
        try {
            return Token.retrieve(stripeToken);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on retrieving token for stripeToken " + stripeToken);
        }
    }

    public void addCardToCustomer(Customer customer, String stripeToken) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", stripeToken);
        try {
            customer.getSources().create(params);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on adding new card to customerId " + customer.getId());
        }
    }

    public Refund refund(String chargeId) {
        Map<String, Object> refundParams = new HashMap<>();
        refundParams.put("charge", chargeId);
        try {
            return Refund.create(refundParams);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on charging card for chargeId " + chargeId);
        }
    }


}
