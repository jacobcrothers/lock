package binar.box.service;

import binar.box.domain.User;
import binar.box.domain.UserAuthority;
import binar.box.dto.*;
import binar.box.repository.AuthorityRepository;
import binar.box.repository.UserRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Autowired
    private Environment environment;

    private ExecutorService executors = Executors.newFixedThreadPool(2);


    public TokenDTO registerUser(@Valid UserDTO userDTO) {
        checkIfUserIsAlreadyRegistered(userDTO.getEmail());
        User user = new User(userDTO);
        user.setPassword(BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(12)));
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

    public TokenDTO loginUser(UserLoginDTO userLoginDTO, boolean rememberMe) {
        User user;
        try {
            user = getUserByEmail(userLoginDTO.getEmail());
        } catch (LockBridgesException e) {
            throw new LockBridgesException(Constants.BAD_CREDENTIALS);
        }
        if (user.getPassword() == null) {
            throw new LockBridgesException(Constants.BAD_CREDENTIALS);
        } else if (!BCrypt.checkpw(userLoginDTO.getPassword(), user.getPassword())) {
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

    public void changeUserPassword(String token, ResetPasswordDTO resetPasswordDTO) {
        User user = getUserByResetPasswordToken(token);
        user.setPassword(BCrypt.hashpw(resetPasswordDTO.getPassword(), BCrypt.gensalt(12)));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    private User getUserByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token).orElseThrow(() -> new LockBridgesException(Constants.USER_NOT_FOUND));
    }

    public TokenDTO renewUserToken() {
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

    public TokenDTO loginUser(FacebookTokenDTO facebookTokenDTO) {
        var accessToken = facebookTokenDTO.getToken();
        var facebook = new FacebookTemplate(accessToken);
        var facebookUserFields = new String[]{Constants.FACEBOOK_ID, Constants.FACEBOOK_EMAIL, Constants.FACEBOOK_FIRST_NAME, Constants.FACEBOOK_LAST_NAME, Constants.FACEBOOK_HOMETOWN, Constants.FACEBOOK_LOCALE};
        var facebookUser = facebook.fetchObject(Constants.FACEBOOK_ME, org.springframework.social.facebook.api.User.class, facebookUserFields);
        var user = userRepository.findByFacebookId(facebookUser.getId());
        if (user.isPresent()) {
            var registeredUser = user.get();
            executors.submit(() -> getLongLiveFacebookToken(registeredUser));
            return tokenService.createUserToken(registeredUser, true);
        }
        var toRegisterUser = new User();
        facebookUserToOurUser(facebookUser, toRegisterUser, accessToken);
        userRepository.save(toRegisterUser);
        executors.submit(() -> getLongLiveFacebookToken(toRegisterUser));
        return tokenService.createUserToken(toRegisterUser, true);
    }

    private void getLongLiveFacebookToken(User user) {
        var facebookTemplate = new FacebookTemplate(user.getFacebookAccessToken());
        var clientId = environment.getProperty(Constants.FACEBOOK_CLIENT_ID);
        var clientSecret = environment.getProperty(Constants.FACEBOOK_CLIENT_SECRET);
        var uri = URIBuilder
                .fromUri(Constants.HTTPS_GRAPH_FACEBOOK_COM_OAUTH_ACCESS_TOKEN)
                .queryParam(Constants.GRANT_TYPE, Constants.FB_EXCHANGE_TOKEN)
                .queryParam(Constants.CLIENT_ID, clientId)
                .queryParam(Constants.CLIENT_SECRET, clientSecret)
                .queryParam(Constants.FB_EXCHANGE_TOKEN, user.getFacebookAccessToken()).build();
        var facebookLongLiveToken = facebookTemplate.restOperations().exchange(uri.toString(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
        var accessToken = facebookLongLiveToken.toString().split(Constants.DOUBLE_QUOTE);
        if (accessToken.length >= 2) {
            user.setFacebookAccessToken(accessToken[3]);
            userRepository.save(user);
        }
    }

    private void facebookUserToOurUser(org.springframework.social.facebook.api.User facebookUser, User toRegisterUser, String accessToken) {
        var userAuthorities = authorityRepository.findByName(Constants.USER_AUTHORITY_STRING);
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

    public UserProfileDTO getUser() {
        return new UserProfileDTO(getAuthenticatedUser());
    }

    public void updateUser(UserProfileDTO userProfileDTO) {
        var user = getAuthenticatedUser();
        user.setLastName(userProfileDTO.getLastName());
        user.setFirstName(userProfileDTO.getFirstName());
        user.setPhone(userProfileDTO.getPhone());
        user.setCity(userProfileDTO.getCity());
        user.setCountry(userProfileDTO.getCountry());
    }

    public void changeUserPassword(ChangePasswordDTO changePasswordDTO) {
        var user = getAuthenticatedUser();
        if (!BCrypt.checkpw(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new LockBridgesException(Constants.OLD_PASSWORD_DOES_NOT_MATCH);
        }
        user.setPassword(BCrypt.hashpw(changePasswordDTO.getPassword(), BCrypt.gensalt(12)));
    }
}
