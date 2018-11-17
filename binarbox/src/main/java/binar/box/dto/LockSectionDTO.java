package binar.box.dto;

import binar.box.domain.Point;
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

	private List<LockResponseDTO> lockResponseDTOs;
}
