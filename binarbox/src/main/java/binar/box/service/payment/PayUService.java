package binar.box.service.payment;

import binar.box.domain.Payment;
import binar.box.dto.payment.payu.PaymentCallback;
import binar.box.dto.payment.payu.PaymentDetail;
import binar.box.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayUService {

    private final PayUGateway payUGateway;

    private final PaymentRepository paymentRepository;

    @Autowired
    public PayUService(PayUGateway payUGateway, PaymentRepository paymentRepository) {
        this.payUGateway = payUGateway;
        this.paymentRepository = paymentRepository;
    }

    public PaymentDetail proceedPayment(PaymentDetail paymentDetail) {
        savePaymentDetail(payUGateway.populatePaymentDetail(paymentDetail));
        return paymentDetail;
    }

    public String payuCallback(PaymentCallback paymentResponse) {
        String msg = "Transaction failed.";
        Payment payment = paymentRepository.findByTxnId(paymentResponse.getTxnid());
        if(payment != null) {
            //TODO validate the hash
            Payment.PaymentStatus paymentStatus = null;
            if(paymentResponse.getStatus().equals("failure")){
                paymentStatus = Payment.PaymentStatus.Failed;
            }else if(paymentResponse.getStatus().equals("success")) {
                paymentStatus = Payment.PaymentStatus.Success;
                msg = "Transaction success";
            }
            payment.setPaymentStatus(paymentStatus);
            payment.setMihpayId(paymentResponse.getMihpayid());
            payment.setMode(paymentResponse.getMode());
            paymentRepository.save(payment);
        }
        return msg;
    }

    private void savePaymentDetail(PaymentDetail paymentDetail) {
        Payment payment = new Payment();
        payment.setAmount(Double.parseDouble(paymentDetail.getAmount()));
        payment.setEmail(paymentDetail.getEmail());
        payment.setName(paymentDetail.getName());
        payment.setPaymentStatus(Payment.PaymentStatus.Pending);
        payment.setPhone(paymentDetail.getPhone());
        payment.setProductInfo(paymentDetail.getProductInfo());
        payment.setTxnId(paymentDetail.getTxnId());
        paymentRepository.save(payment);
    }

}
