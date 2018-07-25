package binar.box.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import binar.box.dto.ChangePasswordDTO;
import binar.box.dto.ResetPasswordDTO;
import binar.box.dto.UserProfileDTO;
import binar.box.service.UserService;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * Created by Timis Nicu Alexandru on 27-Mar-18.
 */
@RestController
@RequestMapping(value = Constants.API)
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping(value = Constants.USER_ENDPOINT + Constants.REQUEST_RESET_PASSWORD_EDNPOINT)
	private void requestResetPassword(@Param("email") String email) {
		userService.requestResetPassword(email);
	}

	@PostMapping(value = Constants.USER_ENDPOINT + Constants.RESET_PASSWORD_ENDPOINT)
	private void resetPassword(@Param("token") String token, @RequestBody @Valid ResetPasswordDTO resetPasswordDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new LockBridgesException(bindingResult.getAllErrors().toString());
		}
		userService.changeUserPassword(token, resetPasswordDTO);
	}

	@PostMapping(value = Constants.USER_ENDPOINT + Constants.CONFIRM_EMAIL_ENDPOINT)
	private void confirmEmail(@Param("token") String token) {
		userService.confirmUserEmail(token);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@GetMapping(value = Constants.USER_ENDPOINT)
	private UserProfileDTO getUser() {
		return userService.getUser();
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@PutMapping(value = Constants.USER_ENDPOINT)
	private void updateUser(@RequestBody UserProfileDTO userProfileDTO) {
		userService.updateUser(userProfileDTO);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@PostMapping(value = Constants.USER_ENDPOINT + Constants.CHANGE_PASSWORD)
	private void changeUserPassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
		userService.changeUserPassword(changePasswordDTO);
	}

}
