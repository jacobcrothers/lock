package binar.box.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LockSectionDTO {

	private String section;

	private Long point;

	private LockResponseDTO lockResponseDTO;
}
