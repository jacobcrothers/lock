package binar.box.converter;

import binar.box.domain.User;
import binar.box.dto.UserProfileDTO;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserConverter {

    public UserProfileDTO userToUserProfileDTO(User user) {
        return UserProfileDTO.builder()
                .address(user.getAddress())
                .city(user.getCity())
                .country(user.getCountry())
                .email(user.getEmail())
                .firstName(user.getAddress())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .build();
    }

    public User facebookUserToUser(org.springframework.social.facebook.api.User facebookUser) {
        User toRegisterUser = new User();
        toRegisterUser.setId(facebookUser.getId());
        toRegisterUser.setEmail(facebookUser.getEmail());
        toRegisterUser.setLastName(facebookUser.getLastName());
        toRegisterUser.setFirstName(facebookUser.getFirstName());
        toRegisterUser.setCountry(facebookUser.getLocale() == null ? null : facebookUser.getLocale().getCountry());
        toRegisterUser.setCity(facebookUser.getLocation() == null ? null : facebookUser.getLocation().getName());
        toRegisterUser.setCreatedDate(new Date());
        toRegisterUser.setLastModifiedDate(new Date());
        return toRegisterUser;
    }
}
