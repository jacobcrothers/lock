package binar.box.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class LockTypeDTOResponse {

	private long id;

	private String type;

	private BigDecimal price;

	private List<FileDTO> filesDTO;

	private List<LockTypeTemplateDTO> lockTypeTemplate;
}