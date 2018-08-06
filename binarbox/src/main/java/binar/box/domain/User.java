package binar.box.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import binar.box.dto.UserDTO;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
@Entity
@Table(name = "user")
public class User extends BaseEntity {

	@Id
	@Column(name = "id")
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

	public User() {
	}

	public User(UserDTO userDTO) {
		this.lastName = userDTO.getLastName();
		this.firstName = userDTO.getFirstName();
		this.email = userDTO.getEmail();
		this.phone = userDTO.getPhone();
		this.city = userDTO.getCity();
		this.country = userDTO.getCountry();
		this.address = userDTO.getAddress();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
