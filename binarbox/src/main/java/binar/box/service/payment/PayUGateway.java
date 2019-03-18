package binar.box.service.payment;

import binar.box.dto.payment.payu.PaymentDetail;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Component
public class PayUGateway {
    private static final String paymentKey = "your-key";

    private static final String paymentSalt = "your-salt";

    private static final String sUrl = "https://localhost:6060/api/v0/payu/payment-response";

    private static final String fUrl = "https://localhost:6060/api/v0/payu/payment-response";

    public PaymentDetail populatePaymentDetail(PaymentDetail paymentDetail){
        String hashString = "";
        Random rand = new Random();
        String randomId = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        String txnId = "Dev" + hashCal("SHA-256", randomId).substring(0, 12);
        paymentDetail.setTxnId(txnId);
        //String otherPostParamSeq = "phone|surl|furl|lastname|curl|address1|address2|city|state|country|zipcode|pg";
        String hashSequence = "key|txnid|amount|productinfo|firstname|email|||||||||||";
        hashString = hashSequence.concat(paymentSalt);
        hashString = hashString.replace("key", paymentKey);
        hashString = hashString.replace("txnid", txnId);
        hashString = hashString.replace("amount", paymentDetail.getAmount());
        hashString = hashString.replace("productinfo", paymentDetail.getProductInfo());
        hashString = hashString.replace("firstname", paymentDetail.getName());
        hashString = hashString.replace("email", paymentDetail.getEmail());

        paymentDetail.setHash(hashCal("SHA-512", hashString));
        paymentDetail.setFUrl(fUrl);
        paymentDetail.setSUrl(sUrl);
        paymentDetail.setKey(paymentKey);
        return paymentDetail;
    }

    public String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte[] messageDigest = algorithm.digest();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }

        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

}
