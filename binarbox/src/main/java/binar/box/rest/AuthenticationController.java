package binar.box.rest;

import binar.box.dto.TokenDto;
import binar.box.dto.UserDto;
import binar.box.dto.UserLoginDto;
import binar.box.service.UserService;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
@RestController
@RequestMapping(value = Constants.API + Constants.AUTHENTICATION_ENDPOINT)
public class AuthenticationController {

    @Autowired
    private UserService userService;


    @PostMapping(value = Constants.REGISTER_ENDPOINT)
    private TokenDto register(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new LockBridgesException(bindingResult.getAllErrors().toString());
        }
        return userService.registerUser(userDto);
    }

    @PostMapping(value = Constants.LOGIN_ENDPOINT)
    private TokenDto login(@RequestBody @Valid UserLoginDto userLoginDto, BindingResult bindingResult, @Param("rememberMe") boolean rememberMe) {
        if (bindingResult.hasErrors()) {
            throw new LockBridgesException(bindingResult.getAllErrors().toString());
        }
        return userService.loginUser(userLoginDto, rememberMe);
    }


    @PostMapping(value = Constants.RENEW_TOKEN_ENDPOINT)
    private TokenDto renewToken() {
        return userService.renewUserToken();
    }
}
