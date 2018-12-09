package binar.box.dto.payment;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaypalDTO {
    private String paymentMethodNonce;
    private Long lockId;
    private BigDecimal amount;
    private String transactionID;
}
