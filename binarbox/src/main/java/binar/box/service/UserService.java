package binar.box.service;

import binar.box.domain.User;
import binar.box.domain.UserAuthority;
import binar.box.dto.*;
import binar.box.repository.AuthorityRepository;
import binar.box.repository.UserRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private AuthorityRepository authorityRepository;


    public TokenDto registerUser(@Valid UserDto userDto) {
        checkIfUserIsAlreadyRegistered(userDto.getEmail());
        User user = new User(userDto);
        user.setPassword(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(12)));
        user.setCreatedDate(new Date());
        user.setLastModifiedDate(new Date());
        String emailToken = UUID.randomUUID().toString();
        user.setConfirmEmailToken(emailToken);
        List<UserAuthority> userAuthorities = authorityRepository.findByName(Constants.USER_AUTHORITY_STRING);
        user.setAuthority(userAuthorities);
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
        if (user.getPassword() == null) {
            throw new LockBridgesException(Constants.BAD_CREDENTIALS);
        } else if (!BCrypt.checkpw(userLoginDto.getPassword(), user.getPassword())) {
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

    User getAuthenticatedUser() {
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

    public TokenDto loginUser(FacebookTokenDto facebookTokenDto) {
        String accessToken = facebookTokenDto.getToken();
        Facebook facebook = new FacebookTemplate(accessToken);
        org.springframework.social.facebook.api.User facebookUser = facebook.userOperations().getUserProfile();
        Optional<User> user = userRepository.findByFacebookId(facebookUser.getId());
        if (user.isPresent()) {
            User registeredUser = user.get();
            registeredUser.setFacebookAccessToken(accessToken);
            userRepository.save(registeredUser);
            return tokenService.createUserToken(registeredUser, true);
        }
        User toRegisterUser = new User();
        facebookUserToOurUser(facebookUser, toRegisterUser, accessToken);
        userRepository.save(toRegisterUser);
        return tokenService.createUserToken(toRegisterUser, true);
    }

    private void facebookUserToOurUser(org.springframework.social.facebook.api.User facebookUser, User toRegisterUser, String accessToken) {
        List<UserAuthority> userAuthorities = authorityRepository.findByName(Constants.USER_AUTHORITY_STRING);
        toRegisterUser.setAuthority(userAuthorities);
        toRegisterUser.setEmail(facebookUser.getEmail());
        toRegisterUser.setLastName(facebookUser.getLastName());
        toRegisterUser.setFirstName(facebookUser.getFirstName());
        toRegisterUser.setCountry(facebookUser.getLocale() == null ? null : facebookUser.getLocale().getCountry());
        toRegisterUser.setCity(facebookUser.getHometown() == null ? null : facebookUser.getHometown().getName());
        toRegisterUser.setFacebookId(facebookUser.getId());
        toRegisterUser.setFacebookAccessToken(accessToken);
        toRegisterUser.setCreatedDate(new Date());
        toRegisterUser.setLastModifiedDate(new Date());
    }

}
