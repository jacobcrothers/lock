package binar.box.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDTO {

	private String lastName;

	private String firstName;

	@NotEmpty
	@NotNull
	private String email;

	private String phone;

	@NotEmpty
	@NotNull
	private String password;

	private String city;

	private String country;

	private String address;
}
