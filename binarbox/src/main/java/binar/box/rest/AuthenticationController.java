package binar.box.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import binar.box.dto.FacebookTokenDTO;
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

	@PostMapping(value = Constants.FACEBOOK_ENDPOINT)
	private void facebookLogin(@RequestBody @Valid FacebookTokenDTO facebookTokenDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new LockBridgesException(bindingResult.getAllErrors().toString());
		}
		userService.loginUser(facebookTokenDTO);
	}
}
