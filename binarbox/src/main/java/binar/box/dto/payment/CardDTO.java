package binar.box.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CardDTO {

    private String id;
    private String name;
    private String expMonth;
    private String expYear;
    private String last4;

    @JsonIgnore
    private String fingerprint;
}
