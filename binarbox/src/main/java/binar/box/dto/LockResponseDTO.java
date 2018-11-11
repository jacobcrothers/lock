package binar.box.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LockResponseDTO {

	private long id;

	private String message;

	private Integer fontSize;

	private String fontStyle;

	private String fontColor;

	private String lockColor;

	private LockSectionDTO lockSection;

	private LockCategoryDTOResponse lockCategoryDTOResponse;

	private Long panelId;

	private Boolean privateLock;

	private Boolean glitteringLight;

	private BigDecimal price;

	private boolean paid;
}
