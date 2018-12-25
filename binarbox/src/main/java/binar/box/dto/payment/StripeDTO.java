package binar.box.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class StripeDTO {

    @NotNull
    private Boolean saveCard;

    public enum Currency {
        EUR, USD
    }

    @JsonIgnore //this is set by server
    private int amount;

    private Currency currency;

    private Long lockId;

    private String chargeId;

    @NotNull
    private String stripeToken;

    @NotNull
    private String cardId;

}
