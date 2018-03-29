package binar.box.rest;

import binar.box.dto.ResetPasswordDto;
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
 * Created by Timis Nicu Alexandru on 27-Mar-18.
 */
@RestController
@RequestMapping(value = Constants.API + Constants.USER)
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping(value = Constants.REQUEST_RESET_PASSWORD_EDNPOINT)
    private void requestResetPassword(@Param("email") String email) {
        userService.requestResetPassword(email);
    }


    @PostMapping(value = Constants.RESET_PASSWORD_ENDPOINT)
    private void resetPassword(@Param("token") String token, @RequestBody @Valid ResetPasswordDto resetPasswordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new LockBridgesException(bindingResult.getAllErrors().toString());
        }
        userService.changeUserPassword(token, resetPasswordDto);
    }

    @PostMapping(value = Constants.CONFIRM_EMAIL_ENDPOINT)
    private void confirmEmail(@Param("token") String token) {
        userService.confirmUserEmail(token);
    }

}
