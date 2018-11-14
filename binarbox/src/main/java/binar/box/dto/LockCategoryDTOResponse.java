package binar.box.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class LockCategoryDTOResponse {

	private long id;

	private String category;

	private BigDecimal price;

	private FileDTO fileDTO;

	private List<LockTypeTemplateDTO> lockTypeTemplate;
}