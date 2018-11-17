package binar.box.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@ToString
@Entity
@Table(name = "lock_section")
public class LockSection extends BaseEntity{

	@Column(name = "section")
	private String section;



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "panel_id")
	private Panel panel;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lockSection")
	private List<Lock> locks;
}
