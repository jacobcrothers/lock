package binar.box.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public class LockTypeDTO {

    @NotEmpty
    @NotNull
    private String type;

    @NotNull
    private BigDecimal price;

    public LockTypeDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
