package binar.box.rest;

import binar.box.dto.TokenDto;
import binar.box.dto.UserDto;
import binar.box.service.UserService;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = Constants.API + Constants.AUTHENTICATION)
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
}
