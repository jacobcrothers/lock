package binar.box.dto;

import binar.box.domain.User;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
public class UserProfileDTO {


    private String lastName;

    private String firstName;

    private String phone;

    private String city;

    private String country;

    public UserProfileDTO() {
    }

    public UserProfileDTO(User user) {
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.phone = user.getPhone();
        this.city = user.getCity();
        this.country = user.getCountry();
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
}
