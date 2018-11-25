package binar.box.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class LockTemplateDTO {

	private Long id;

	private Integer fontSize;

	private String fontStyle;

	private String fontColor;

	private List<FileDTO> filesDTO;

	private List<FontDTO> fontsDTO;

	private String lockCategory;

	private BigDecimal price;
}
