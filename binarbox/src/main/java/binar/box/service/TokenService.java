package binar.box.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import binar.box.domain.User;

/**
 * Created by Timis Nicu Alexandru on 21-Mar-18.
 */
@Service
@Transactional
public class TokenService {

	@Autowired
	private UserService userService;

	public User checkFacebookToken(String token) {
		return userService.checkUserIfRegistered(token);
	}

}
