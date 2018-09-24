package binar.box.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by Timis Nicu Alexandru on 11-Jun-18.
 */
@Entity
@Table(name = "panel_entity")
public class Panel extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Transient
	private List<LockSection> lockSection;

	public Panel() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<LockSection> getLockSection() {
		return lockSection;
	}

	public void setLockSection(List<LockSection> lockSection) {
		this.lockSection = lockSection;
	}

}
