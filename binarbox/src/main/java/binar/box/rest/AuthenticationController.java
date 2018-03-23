package binar.box.rest;

import binar.box.dto.TokenDto;
import binar.box.dto.UserDto;
import binar.box.service.UserService;
import binar.box.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
@RestController(value = Constants.API)
public class AuthenticationController {

    @Autowired
    private UserService userService;


    @PostMapping(value = Constants.AUTHENTICATION)
    private TokenDto register(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        return userService.registerUser(userDto);
    }


}
