package binar.box.service;

import binar.box.domain.User;
import binar.box.dto.ResetPasswordDto;
import binar.box.dto.TokenDto;
import binar.box.dto.UserDto;
import binar.box.dto.UserLoginDto;
import binar.box.repository.UserRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Timis Nicu Alexandru on 21-Mar-18.
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;


    public TokenDto registerUser(@Valid UserDto userDto) {
        checkIfUserIsAlreadyRegistered(userDto.getEmail());
        User user = new User(userDto);
        user.setPassword(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(12)));
        user.setCreatedDate(new Date());
        user.setLastModifiedDate(new Date());
        String emailToken = UUID.randomUUID().toString();
        user.setConfirmEmailToken(emailToken);
        emailService.sendEmail(user.getEmail(), "Welcome", "Welcome to Lock Bridges : please confirm email : " +
                emailToken);
        userRepository.save(user);
        return tokenService.createUserToken(user, true);
    }

    private void checkIfUserIsAlreadyRegistered(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new LockBridgesException(Constants.USER_ALREADY_REGISTERED);
        }
    }

    public TokenDto loginUser(UserLoginDto userLoginDto, boolean rememberMe) {
        User user;
        try {
            user = getUserByEmail(userLoginDto.getEmail());
        } catch (LockBridgesException e) {
            throw new LockBridgesException(Constants.BAD_CREDENTIALS);
        }

        if (!BCrypt.checkpw(userLoginDto.getPassword(), user.getPassword())) {
            throw new LockBridgesException(Constants.BAD_CREDENTIALS);
        }
        return tokenService.createUserToken(user, rememberMe);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new LockBridgesException(Constants.USER_NOT_FOUND));
    }

    public void requestResetPassword(String email) {
        User user = getUserByEmail(email);
        String resetPasswordToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetPasswordToken);
        emailService.sendEmail(user.getEmail(), "Reset Password", resetPasswordToken);
        userRepository.save(user);
    }

    public void changeUserPassword(String token, ResetPasswordDto resetPasswordDto) {
        User user = getUserByResetPasswordToken(token);
        user.setPassword(BCrypt.hashpw(resetPasswordDto.getPassword(), BCrypt.gensalt(12)));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    private User getUserByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token).orElseThrow(() -> new LockBridgesException(Constants.USER_NOT_FOUND));
    }

    public TokenDto renewUserToken() {
        User user = getAuthenticatedUser();
        return tokenService.createUserToken(user, true);
    }

    private User getAuthenticatedUser() {
        return getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void confirmUserEmail(String token) {
        User user = getUserByEmailToken(token);
        user.setConfirmEmailToken(null);
        user.setEmailConfirmed(true);
        userRepository.save(user);
    }

    private User getUserByEmailToken(String token) {
        return userRepository.findByConfirmEmailToken(token).orElseThrow(() -> new LockBridgesException(Constants.USER_NOT_FOUND));
    }
}
