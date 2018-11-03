package binar.box.domain;

import java.util.Date;

import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@MappedSuperclass
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@CreatedDate
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private Date createdDate;

	@LastModifiedDate
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_modified_date")
	private Date lastModifiedDate;
}
