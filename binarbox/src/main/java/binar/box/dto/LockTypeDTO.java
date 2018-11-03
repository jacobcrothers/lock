package binar.box.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class LockTypeDTO {

	@NotEmpty
	@NotNull
	private String type;

	@NotNull
	private BigDecimal price;
}
