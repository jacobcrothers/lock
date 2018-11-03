package binar.box.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class LockTypeTemplateDTO {

	private Long id;

	private Integer fontSize;

	private String fontStyle;

	private String fontColor;

	private List<FileDTO> filesDTO;

	private String lockType;

	private BigDecimal price;
}
