package binar.box.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import binar.box.dto.UserProfileDTO;
import binar.box.service.UserService;
import binar.box.util.Constants;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Created by Timis Nicu Alexandru on 27-Mar-18.
 */
@RestController
@RequestMapping(value = Constants.API)
public class UserController {

	@Autowired
	private UserService userService;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get user info", notes = "Endpoint to get user last name,first name and other user-related information")
	@GetMapping(value = Constants.USER_ENDPOINT)
	private UserProfileDTO getUser() {
		return userService.getUser();
	}
}
