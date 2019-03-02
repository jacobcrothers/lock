package binar.box.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class LockSectionDTO {

	@NotNull
	private String section;

	private List<LockResponseDTO> lockResponseDTOs;
}
