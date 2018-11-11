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
@Table(name = "lock_type_template")
public class LockTypeTemplate extends BaseEntity {

	@Column(name = "font_size")
	private Integer fontSize;

	@Column(name = "font_style")
	private String fontStyle;

	@Column(name = "font_color")
	private String fontColor;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lockTypeTemplate")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<File> files;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lock_category_id")
	private LockCategory lockCategory;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lockTypeTemplate")
	private List<Lock> locks;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "price_id")
	private Price price;
}
