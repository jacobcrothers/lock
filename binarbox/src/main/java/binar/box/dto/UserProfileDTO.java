package binar.box.dto;

import binar.box.domain.User;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
public class UserProfileDTO {

	private String lastName;

	private String firstName;

	private String email;

	private String phone;

	private String city;

	private String country;

	private String address;

	private boolean linkedWithFacebbok;

	public UserProfileDTO() {
	}

	public UserProfileDTO(User user) {
		this.lastName = user.getLastName();
		this.firstName = user.getFirstName();
		this.phone = user.getPhone();
		this.city = user.getCity();
		this.country = user.getCountry();
		this.email = user.getEmail();
		this.address = user.getAddress();
		this.linkedWithFacebbok = user.isLinkedWithFacebbok();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
