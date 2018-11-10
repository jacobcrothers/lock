package binar.box.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDTO {

	private String lastName;

	private String firstName;

	private String email;

	private String phone;

	private String city;

	private String country;

	private String address;
}
