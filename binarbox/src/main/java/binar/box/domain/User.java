package binar.box.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "user")
public class User{

	@Id
	//known mysql limitation -> utf8mb4 - 4 bytes per character instead of 3
	@Column(length = 191)
	private String id;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "email")
	private String email;

	@Column(name = "phone")
	private String phone;

	@Column(name = "city")
	private String city;

	@Column(name = "country")
	private String country;

	@Column(name = "address")
	private String address;

	@Column(name = "locale")
	private String locale= Locale.getDefault().toLanguageTag();

	@CreatedDate
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private Date createdDate;

	@LastModifiedDate
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_modified_date")
	private Date lastModifiedDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Lock> locks;

	@Column(name = "stripe_costumer_id")
	private String stripeCustomerId;

	@Column(name= "has_agreed_to_terms")
	private Boolean hasAgreedToTerms = false;
}
