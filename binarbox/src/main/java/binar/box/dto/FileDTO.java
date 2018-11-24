package binar.box.dto;

import binar.box.domain.File;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDTO {

	private long id;

	private String name;

	private String urlToFile;

	private String pathToFile;

	private File.Type type;
}
