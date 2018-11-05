package binar.box.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@ToString
@Entity
@Table(name = "lock_type")
//@NamedEntityGraph(
//		name = "LockType.templateAndFiles",
//		attributeNodes = {
//				@NamedAttributeNode(value = "lockTypeTemplate"),
//				@NamedAttributeNode(value = "price"),
//				@NamedAttributeNode(value = "files")
//		}
//)
public class LockType extends BaseEntity {

	@Column(name = "lock_type")
	private String type;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lockType")
	private List<File> files;

	@Column(name = "total_rating")
	private Float totalRating;

	@OneToMany(mappedBy = "lockType", fetch = FetchType.LAZY)
	private List<LockTypeTemplate> lockTypeTemplate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "price")
	private Price price;
}
