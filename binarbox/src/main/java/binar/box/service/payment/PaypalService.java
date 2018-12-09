package binar.box.service.payment;

import binar.box.domain.Lock;
import binar.box.dto.payment.PaypalDTO;
import binar.box.repository.LockRepository;
import binar.box.util.Exceptions.PaymentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Objects;

@Service
public class PaypalService {

    @Autowired
    private PaypalGateway paypalGateway;

    @Autowired
    private LockRepository lockRepository;

    public PaypalDTO createTransaction(PaypalDTO payPalDTO) {
        Lock lock = lockRepository.getOne(payPalDTO.getLockId());
        if (StringUtils.isEmpty(payPalDTO.getPaymentMethodNonce()) || Objects.isNull(lock.getLockTemplate())) {
            throw new PaymentException("Amount or payment method nonce empty","amount.or.nonce.empty");
        }

        lock.setPaid(true);
        lockRepository.save(lock);

        return PaypalDTO.builder()
                .transactionID(paypalGateway.createTransaction(payPalDTO.getPaymentMethodNonce(), lock.getLockTemplate().getPrice().getPrice()))
                .lockId(lock.getId())
                .amount(lock.getLockTemplate().getPrice().getPrice())
                .build();
    }

    public void refundTransaction(PaypalDTO payPalDTO) {
        Lock lock = lockRepository.getOne(payPalDTO.getLockId());

        if (StringUtils.isEmpty(payPalDTO.getTransactionID()) || Objects.isNull(lock.getLockTemplate())) {
            throw new PaymentException("Transaction id empty","transaction.id.empty");
        }
        paypalGateway.refundTransaction(payPalDTO.getTransactionID());

        lock.setPaid(false);
        lockRepository.save(lock);
    }
}
