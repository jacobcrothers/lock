package binar.box.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDTO {
    private String paymentMethodNonce;
    private BigDecimal amount;
    private String transactionID;
}
