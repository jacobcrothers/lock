package binar.box.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class FacebookTokenDTO {

	@NotEmpty
	@NotNull
	private String token;
}
