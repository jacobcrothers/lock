package binar.box.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
@Entity
@Table(name = "file")
public class File extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "path_to_file")
	private String pathToFile;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lock_type")
	private LockType lockType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lock_type_template")
	private LockTypeTemplate lockTypeTemplate;

	public File() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPathToFile() {
		return pathToFile;
	}

	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	public LockType getLockType() {
		return lockType;
	}

	public void setLockType(LockType lockType) {
		this.lockType = lockType;
	}

	public LockTypeTemplate getLockTypeTemplate() {
		return lockTypeTemplate;
	}

	public void setLockTypeTemplate(LockTypeTemplate lockTypeTemplate) {
		this.lockTypeTemplate = lockTypeTemplate;
	}
}
