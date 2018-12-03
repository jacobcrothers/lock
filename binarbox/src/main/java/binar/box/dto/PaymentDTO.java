package binar.box.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentDTO {
    private String paymentMethodNonce;
    private BigDecimal ammount;
    private String transactionID;
}
