package binar.box.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import binar.box.domain.User;

@Service
@Transactional
public class TokenService {

	private final UserService userService;

	@Autowired
	public TokenService(UserService userService) {
		this.userService = userService;
	}

	public User checkFacebookToken(String token) {
		return userService.checkUserIfRegistered(token);
	}

}
