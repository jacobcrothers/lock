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
@Table(name = "lock_category")
public class LockCategory extends BaseEntity {

	@Column(name = "category")
	private String category;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lockCategory")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<File> files;

	@Column(name = "total_rating")
	private Float totalRating;

	@OneToMany(mappedBy = "lockCategory", fetch = FetchType.LAZY)
	private List<LockTypeTemplate> lockTypeTemplate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "price_id")
	private Price price;
}
