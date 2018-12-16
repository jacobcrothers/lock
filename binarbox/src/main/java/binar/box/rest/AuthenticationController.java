package binar.box.rest;

import javax.validation.Valid;

import binar.box.util.Exceptions.FieldsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import binar.box.dto.FacebookTokenDTO;
import binar.box.service.UserService;
import binar.box.util.Constants;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = Constants.API + Constants.AUTHENTICATION_ENDPOINT)
public class AuthenticationController {

	private final UserService userService;

	@Autowired
	public AuthenticationController(UserService userService) {
		this.userService = userService;
	}

	@ApiOperation(value = "Authenticate using the Facebook token", notes = "This endpoint uses the Facebook token to register the user in the database. "
			+ "If the user already exists nothing happens. "
			+ "After this step, the Facebook token can be set on the token header to authorize user requests.")
	@PostMapping(value = Constants.FACEBOOK_ENDPOINT)
	private ResponseEntity facebookLogin(@Valid @RequestBody FacebookTokenDTO facebookTokenDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new FieldsException("Facebook token not valid", "facebook.token.invalid", bindingResult);
		}
		userService.loginUser(facebookTokenDTO);
		return new ResponseEntity(HttpStatus.OK);
	}
}
