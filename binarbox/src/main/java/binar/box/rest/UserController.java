package binar.box.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import binar.box.dto.UserProfileDTO;
import binar.box.service.UserService;
import binar.box.util.Constants;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = Constants.API)
public class UserController {

	@Autowired
	private UserService userService;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get user info", notes = "Endpoint to get user last name,first name and other user-related information")
	@GetMapping(value = Constants.USER_ENDPOINT)
	@ResponseStatus(HttpStatus.OK)
	private UserProfileDTO getUser() {
		return userService.getUser();
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "User accepts terms and conditions", notes = "Set user agrees to terms and conditions")
	@RequestMapping(value = Constants.USER_ENDPOINT + "/acceptTerms", method = RequestMethod.POST)
	public ResponseEntity<?> acceptTermsAndConditions() {
		userService.acceptTermsAndConditions();
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
