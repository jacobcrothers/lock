package binar.box.rest;

import binar.box.dto.ChangePasswordDTO;
import binar.box.dto.ResetPasswordDto;
import binar.box.dto.UserProfileDto;
import binar.box.service.UserService;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    private void resetPassword(@Param("token") String token, @RequestBody @Valid ResetPasswordDto resetPasswordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new LockBridgesException(bindingResult.getAllErrors().toString());
        }
        userService.changeUserPassword(token, resetPasswordDto);
    }

    @PostMapping(value = Constants.USER_ENDPOINT + Constants.CONFIRM_EMAIL_ENDPOINT)
    private void confirmEmail(@Param("token") String token) {
        userService.confirmUserEmail(token);
    }

    @GetMapping(value = Constants.USER_ENDPOINT)
    private UserProfileDto getUser() {
        return userService.getUser();
    }

    @PutMapping(value = Constants.USER_ENDPOINT)
    private void updateUser(@RequestBody UserProfileDto userProfileDto) {
        userService.updateUser(userProfileDto);
    }

    @PostMapping(value = Constants.USER_ENDPOINT + Constants.CHANGE_PASSWORD)
    private void changeUserPassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changeUserPassword(changePasswordDTO);
    }

}
