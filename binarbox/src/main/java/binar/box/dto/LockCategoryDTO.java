package binar.box.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Data
public class LockCategoryDTO {

	@NotEmpty
	@NotNull
	private String category;

	@NotNull
	private BigDecimal price;
}
