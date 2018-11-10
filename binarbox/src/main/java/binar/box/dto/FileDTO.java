package binar.box.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDTO {

	private long id;

	private String name;
}
