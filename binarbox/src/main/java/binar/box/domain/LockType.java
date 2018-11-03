package binar.box.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@ToString
@Entity
@Table(name = "lock_type")
public class LockType extends BaseEntity {

	@Column(name = "lock_type")
	private String type;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "lockType")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<File> files;

	@Column(name = "total_rating")
	private Float totalRating;

	@OneToMany(mappedBy = "lockType")
	private List<LockTypeTemplate> lockTypeTemplate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "price")
	private Price price;
}
