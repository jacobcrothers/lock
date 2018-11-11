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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lock_category_id")
	private LockCategory lockCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lock_type_template_id")
	private LockTypeTemplate lockTypeTemplate;
}
