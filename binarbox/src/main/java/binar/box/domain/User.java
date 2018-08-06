package binar.box.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "email")
	private String email;

	@Column(name = "phone")
	private String phone;

	@Column(name = "password")
	private String password;

	@Column(name = "reset_password_token")
	private String resetPasswordToken;

	@Column(name = "city")
	private String city;

	@Column(name = "country")
	private String country;

	@Column(name = "confirm_email_token")
	private String confirmEmailToken;

	@Column(name = "email_confirmed")
	private boolean emailConfirmed;

	@Column(name = "facebook_id")
	private String facebookId;

	@Column(name = "facebook_access_token")
	private String facebookAccessToken;

	@Column(name = "address")
	private String address;

	@Column(name = "linked_with_facebook")
	private boolean linkedWithFacebbok;

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

	public String getFacebookAccessToken() {
		return facebookAccessToken;
	}

	public void setFacebookAccessToken(String facebookAccessToken) {
		this.facebookAccessToken = facebookAccessToken;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getConfirmEmailToken() {
		return confirmEmailToken;
	}

	public void setConfirmEmailToken(String confirmEmailToken) {
		this.confirmEmailToken = confirmEmailToken;
	}

	public boolean isEmailConfirmed() {
		return emailConfirmed;
	}

	public void setEmailConfirmed(boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
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

	public boolean isLinkedWithFacebbok() {
		return linkedWithFacebbok;
	}

	public void setLinkedWithFacebbok(boolean linkedWithFacebbok) {
		this.linkedWithFacebbok = linkedWithFacebbok;
	}
}
