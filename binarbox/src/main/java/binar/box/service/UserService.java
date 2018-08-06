package binar.box.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import binar.box.domain.User;
import binar.box.dto.FacebookTokenDTO;
import binar.box.dto.UserProfileDTO;
import binar.box.repository.UserRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;

/**
 * Created by Timis Nicu Alexandru on 21-Mar-18.
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	private User getUserById(String userId) {
		return userRepository.findById(userId).orElseThrow(() -> new LockBridgesException(Constants.USER_NOT_FOUND));
	}

	User getAuthenticatedUser() {
		return getUserById(SecurityContextHolder.getContext().getAuthentication().getName());
	}

	public User loginUser(FacebookTokenDTO facebookTokenDTO) {
		var faceBookUser = getFacebookUser(facebookTokenDTO.getToken(),
				new String[] { Constants.FACEBOOK_ID, Constants.FACEBOOK_EMAIL, Constants.FACEBOOK_FIRST_NAME,
						Constants.FACEBOOK_LAST_NAME, Constants.LOCATION });
		var user = getFacebookUserFromDataIfRegistered(faceBookUser);
		if (user.isPresent()) {
			return user.get();
		}
		var toRegisterUser = new User();
		facebookUserToOurUser(faceBookUser, toRegisterUser, facebookTokenDTO.getToken());
		return userRepository.save(toRegisterUser);
	}

	private Optional<User> getFacebookUserFromDataIfRegistered(
			org.springframework.social.facebook.api.User faceBookUser) {
		return userRepository.findById(faceBookUser.getId());
	}

	private org.springframework.social.facebook.api.User getFacebookUser(String token, String[] facebookUserFields) {
		var facebook = new FacebookTemplate(token);
		var facebookUser = facebook.fetchObject(Constants.FACEBOOK_ME,
				org.springframework.social.facebook.api.User.class, facebookUserFields);
		return facebookUser;
	}

	private void facebookUserToOurUser(org.springframework.social.facebook.api.User facebookUser, User toRegisterUser,
			String accessToken) {
		toRegisterUser.setId(facebookUser.getId());
		toRegisterUser.setEmail(facebookUser.getEmail());
		toRegisterUser.setLastName(facebookUser.getLastName());
		toRegisterUser.setFirstName(facebookUser.getFirstName());
		toRegisterUser.setCountry(facebookUser.getLocale() == null ? null : facebookUser.getLocale().getCountry());
		toRegisterUser.setCity(facebookUser.getLocation() == null ? null : facebookUser.getLocation().getName());
		toRegisterUser.setCreatedDate(new Date());
		toRegisterUser.setLastModifiedDate(new Date());
	}

	public UserProfileDTO getUser() {
		return new UserProfileDTO(getAuthenticatedUser());
	}

	private void getUserFriendFromFacebook(User user, LinkedList<String> idsList) {
		var facebook = new FacebookTemplate(user.getId().toString());
		var facebookUserFields = new String[] { Constants.FACEBOOK_FRIENDS };
		var facebookUser = facebook.fetchObject(Constants.FACEBOOK_ME,
				org.springframework.social.facebook.api.User.class, facebookUserFields);
		var friendsIds = facebookUser.getExtraData().get(Constants.FACEBOOK_FRIENDS).toString();
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(friendsIds);
			JSONArray array = jsonObject.getJSONArray(Constants.DATA);
			if (array.length() > 0) {
				JSONObject jo = array.getJSONObject(0);
				JSONArray jsonFriendsIds = jo.getJSONArray(Constants.ID);
				if (jsonFriendsIds.length() > 0) {
					for (var index = 0; index < jsonFriendsIds.length(); index++) {
						idsList.add(jsonFriendsIds.get(index).toString());
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new LockBridgesException(Constants.FAILED_TO_GET_FRIENDS_LIST);
		}
	}

	public List<String> getUserFacebookFriends(User user) {
		LinkedList<String> idsList = new LinkedList<>();
		getUserFriendFromFacebook(user, idsList);
		return idsList;
	}

	public User checkUserIfRegistered(String token) {
		var faceBookUser = getFacebookUser(token, new String[] { Constants.FACEBOOK_ID });
		return getFacebookUserFromDataIfRegistered(faceBookUser)
				.orElseThrow(() -> new LockBridgesException(Constants.UNAUTHORIZED));
	}
}
