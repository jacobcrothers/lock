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
@Table(name = "lock_template")
public class LockTemplate extends BaseEntity {
	@OneToMany
	@JoinTable(name="TemplateFile",
			joinColumns = @JoinColumn( name="lock_template_id"),
			inverseJoinColumns = @JoinColumn( name="file_id"))
	private List<File> files;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lock_category_id")
	private LockCategory lockCategory;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lockTemplate")
	private List<Lock> locks;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lockTemplate")
	private List<Font> fonts;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "price_id")
	private Price price;
}
