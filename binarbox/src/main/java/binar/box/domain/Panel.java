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
@Table(name = "panel")
public class Panel extends BaseEntity {

	@Column(name="panel_number")
	int panelNumber;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "panel")
	private List<LockSection> lockSections;
}
