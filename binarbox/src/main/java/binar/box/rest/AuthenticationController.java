package binar.box.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import binar.box.dto.FacebookTokenDTO;
import binar.box.dto.TokenDTO;
import binar.box.dto.UserDTO;
import binar.box.dto.UserLoginDTO;
import binar.box.service.UserService;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
@RestController
@RequestMapping(value = Constants.API + Constants.AUTHENTICATION_ENDPOINT)
public class AuthenticationController {

	@Autowired
	private UserService userService;

	@PostMapping(value = Constants.REGISTER_ENDPOINT)
	private void register(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new LockBridgesException(bindingResult.getAllErrors().toString());
		}
		userService.registerUser(userDTO);
	}

	@PostMapping(value = Constants.LOGIN_ENDPOINT)
	private TokenDTO login(@RequestBody @Valid UserLoginDTO userLoginDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new LockBridgesException(bindingResult.getAllErrors().toString());
		}
		return userService.loginUser(userLoginDTO);
	}

	@PostMapping(value = Constants.FACEBOOK_ENDPOINT)
	private TokenDTO facebookLogin(@RequestBody @Valid FacebookTokenDTO facebookTokenDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new LockBridgesException(bindingResult.getAllErrors().toString());
		}
		return userService.loginUser(facebookTokenDTO);
	}

	@PostMapping(value = Constants.RENEW_TOKEN_ENDPOINT)
	private TokenDTO renewToken() {
		return userService.renewUserToken();
	}
}
