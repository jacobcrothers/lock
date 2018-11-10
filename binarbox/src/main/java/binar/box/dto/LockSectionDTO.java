package binar.box.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class LockSectionDTO {

	@NotNull
	private String section;

	// TO MAKE ENTITY FOR X AND Y
	private Long point;


	private List<LockResponseDTO> lockResponseDTOs;
}
