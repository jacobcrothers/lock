package binar.box.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import binar.box.domain.Lock;
import binar.box.domain.User;
import binar.box.dto.FacebookTokenDTO;
import binar.box.dto.UserProfileDTO;
import binar.box.repository.LockRepository;
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
	private LockRepository lockRepository;

	@Autowired
	private UserRepository userRepository;

	private User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new LockBridgesException(Constants.USER_NOT_FOUND));
	}

	User getAuthenticatedUser() {
		return getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
	}

	public void loginUser(FacebookTokenDTO facebookTokenDTO) {
		var accessToken = facebookTokenDTO.getToken();
		var facebook = new FacebookTemplate(accessToken);
		var facebookUserFields = new String[] { Constants.FACEBOOK_ID, Constants.FACEBOOK_EMAIL,
				Constants.FACEBOOK_FIRST_NAME, Constants.FACEBOOK_LAST_NAME, Constants.LOCATION };
		var facebookUser = facebook.fetchObject(Constants.FACEBOOK_ME,
				org.springframework.social.facebook.api.User.class, facebookUserFields);
		var user = userRepository.findByFacebookIdOrEmail(facebookUser.getId(), facebookUser.getEmail());
		if (user.isPresent()) {
			var registeredUser = user.get();
			registeredUser.setEmailConfirmed(true);
			registeredUser.setLinkedWithFacebbok(true);
			userRepository.save(registeredUser);
		}
		var toRegisterUser = new User();
		facebookUserToOurUser(facebookUser, toRegisterUser, accessToken);
		userRepository.save(toRegisterUser);
	}

	private void facebookUserToOurUser(org.springframework.social.facebook.api.User facebookUser, User toRegisterUser,
			String accessToken) {
		toRegisterUser.setEmail(facebookUser.getEmail());
		toRegisterUser.setLastName(facebookUser.getLastName());
		toRegisterUser.setFirstName(facebookUser.getFirstName());
		toRegisterUser.setCountry(facebookUser.getLocale() == null ? null : facebookUser.getLocale().getCountry());
		toRegisterUser.setCity(facebookUser.getLocation() == null ? null : facebookUser.getLocation().getName());
		toRegisterUser.setFacebookId(facebookUser.getId());
		toRegisterUser.setFacebookAccessToken(accessToken);
		toRegisterUser.setCreatedDate(new Date());
		toRegisterUser.setLastModifiedDate(new Date());
		toRegisterUser.setEmailConfirmed(true);
		toRegisterUser.setLinkedWithFacebbok(true);
	}

	public UserProfileDTO getUser() {
		return new UserProfileDTO(getAuthenticatedUser());
	}

	public List<Lock> getUserFriendsLocks(User user) {
		LinkedList<String> idsList = new LinkedList<>();
		getUserFriendFromFacebook(user, idsList);
		var applicationFriends = getFriendsFromDatabase(idsList);
		var friendsLocks = getFriendsLocks(applicationFriends);
		return friendsLocks;
	}

	private void getUserFriendFromFacebook(User user, LinkedList<String> idsList) {
		var facebook = new FacebookTemplate(user.getFacebookAccessToken());
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

	private List<Lock> getFriendsLocks(List<User> applicationFriends) {
		return lockRepository.findByUserInAndPrivateLockFalse(applicationFriends);
	}

	private List<User> getFriendsFromDatabase(LinkedList<String> idsList) {
		return userRepository.findByFacebookIdIn(idsList);
	}

	public List<String> getUserFacebookFriends(User user) {
		LinkedList<String> idsList = new LinkedList<>();
		getUserFriendFromFacebook(user, idsList);
		return idsList;
	}
}
