package binar.box.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@ToString
@Entity
@Table(name = "file")
public class File extends BaseEntity {

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "path_to_file")
	private String pathToFile;

	@Column(name="type")
	private Type type;

	public enum Type {
		CATEGORY,
		FULL_TEMPLATE,
		PARTIALY_ERASED_TEMPLATE,
		PARTIALY_ERASED_TEMPLATE_WITH_TEXT,
		BRIDGE
	}
}

