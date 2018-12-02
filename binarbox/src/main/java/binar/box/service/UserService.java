package binar.box.service;

import binar.box.converter.UserConverter;
import binar.box.domain.User;
import binar.box.dto.FacebookTokenDTO;
import binar.box.dto.UserProfileDTO;
import binar.box.repository.UserRepository;
import binar.box.util.Constants;
import binar.box.util.Exceptions.EntityNotFoundException;
import binar.box.util.Exceptions.LockBridgesException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserConverter userConverter;

	private User getUserById(String userId) {
		return userRepository.findOne(userId);
	}

	public User getAuthenticatedUser() {
		return getUserById(SecurityContextHolder.getContext().getAuthentication().getName());
	}

	public void loginUser(FacebookTokenDTO facebookTokenDTO) {
		var faceBookUser = getFacebookUser(facebookTokenDTO.getToken(),
				new String[] { Constants.FACEBOOK_ID, Constants.FACEBOOK_EMAIL, Constants.FACEBOOK_FIRST_NAME,
						Constants.FACEBOOK_LAST_NAME, Constants.LOCATION });
		getFacebookUserFromDataIfRegistered(faceBookUser).orElseGet(() ->
				userRepository.save(facebookUserToOurUser(faceBookUser)));
	}

	private Optional<User> getFacebookUserFromDataIfRegistered(
			org.springframework.social.facebook.api.User faceBookUser) {
		return userRepository.findById(faceBookUser.getId());
	}

	private org.springframework.social.facebook.api.User getFacebookUser(String token, String[] facebookUserFields) {
		var facebook = new FacebookTemplate(token);
		return facebook.fetchObject(Constants.FACEBOOK_ME,
				org.springframework.social.facebook.api.User.class, facebookUserFields);
	}

	private User facebookUserToOurUser(org.springframework.social.facebook.api.User facebookUser) {
		return userConverter.facebookUserToUser(facebookUser);
	}

	public UserProfileDTO getUser() {
		return userConverter.userToUserProfileDTO(getAuthenticatedUser());
	}

	private List<String> getUserFriendFromFacebook() {
		List<String> idsList = new ArrayList<>();
		var facebook = new FacebookTemplate(getAuthenticatedUserToken());
		var facebookUserFields = new String[] { Constants.FACEBOOK_FRIENDS };
		var facebookUser = facebook.fetchObject(Constants.FACEBOOK_ME,
				org.springframework.social.facebook.api.User.class, facebookUserFields);
		if (facebookUser.getExtraData().isEmpty()){
			return new ArrayList<>();
		}
		var friendsIds = facebookUser.getExtraData().get(Constants.FACEBOOK_FRIENDS).toString();
		JSONObject jsonObject;
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
			throw new LockBridgesException(Constants.FAILED_TO_GET_FRIENDS_LIST, "failed.friends.list");
		}
		return idsList;
	}

	private String getAuthenticatedUserToken() {
		return (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
	}

	public List<String> getUserFacebookFriends() {
		return getUserFriendFromFacebook();
	}

	public User checkUserIfRegistered(String token) {
		var faceBookUser = getFacebookUser(token, new String[] { Constants.FACEBOOK_ID });
		return getFacebookUserFromDataIfRegistered(faceBookUser)
				.orElseThrow(() -> new EntityNotFoundException(Constants.UNAUTHORIZED,"user.unauthorized"));
	}
}
