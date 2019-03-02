package binar.box.dto.payment.payu;

import binar.box.domain.Payment;
import lombok.Data;

@Data
public class PaymentCallback {
    private String txnid;
    private String mihpayid;
    private Payment.PaymentMode mode;
    private String status;
    private String hash;
}
