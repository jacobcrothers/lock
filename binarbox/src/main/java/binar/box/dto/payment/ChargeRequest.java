package binar.box.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChargeRequest {

    @NotNull
    private Boolean saveCard;

    public enum Currency {
        EUR, USD
    }

    @JsonIgnore //this is set by server
    private int amount;

    private Currency currency;

    @NotNull
    private String stripeToken;

    @NotNull
    private String cardId;

}
