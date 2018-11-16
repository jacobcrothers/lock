package binar.box.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "file_id")
	private File file;

	@Column(name = "total_rating")
	private Float totalRating;

	@OneToMany(mappedBy = "lockCategory", fetch = FetchType.LAZY)
	private List<LockTemplate> lockTemplate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "price_id")
	private Price price;
}
