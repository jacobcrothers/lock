package binar.box.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
public class Constants {
	public static final String DATABASE_PASSWORD_PROPERTY = "password";
	public static final String DATABASE_USERNAME_PROPERTY = "user";
	public static final String API = "/api/v0/";
	public static final String AUTHENTICATION_ENDPOINT = "authentication";
	public static final String USER_ALREADY_REGISTERED = "User already registered!";
	public static final String AUTHENTICATION_TOKEN_NOT_FOUND = "Authentication token not found!";
	public static final String TOKEN_NOT_FOUND = "Token not found!";
	public static final String RANDOM_SHA_256_SIGNATURE = "0AEC1B06B077241E9560590684EE3EF4BE49DAC947FF833E98FAA7080C6F1FE8";
	public static final String JWT_PAYLOAD_CLAIM_USER = "user";
	public static final String INVALID_TOKEN = "Invalid token!";
	public static final String TOKEN_HEADER_REQUEST = "token";
	public static final String UNSUPORTED_ENCODING_EXCEPTION = "Unsuported encoding exception : ";
	public static final String SWAGGER_RESOURCES = "/swagger-resources/**";
	public static final String SWAGGER_V2_API_DOCS = "/v2/api-docs";
	public static final String SWAGGER_UI_HTML = "/swagger-ui.html";
	public static final String SWAGGER_WEBJARS = "/webjars/**";
	public static final String REGISTER_ENDPOINT = "/register";
	public static final String LOGIN_ENDPOINT = "/login";
	public static final String USER_NOT_FOUND = "User not found!";
	public static final String BAD_CREDENTIALS = "Bad credentials!";
	public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	public static final String USER_ENDPOINT = "user";
	public static final String REQUEST_RESET_PASSWORD_EDNPOINT = "/request/reset/password";
	public static final String RESET_PASSWORD_ENDPOINT = "/reset/password";
	public static final String CORS_URL_PATTERN = "/**";
	public static final String TOKEN_EXPIRED = "Token expired!";
	public static final String RENEW_TOKEN_ENDPOINT = "/renew";
	public static final String CONFIRM_EMAIL_ENDPOINT = "/email/confirm";
	public static final String USER_AUTHORITY_STRING = "USER";
	public static final String LOCK_ENDPOINT = "lock";
	public static final String GET_LOCKS_BY_USER_ID = "lock/{userId}";
	public static final String LOCK_UNPAID_ENDPOINT = "lock/unpaid";
	public static final String UPDATE_LOCK_SECTION_ENDPOINT = "lock/{lockId}/section/{sectionId}";
	public static final String UPDATE_LOCK_PAID_ENDPOINT = "lock/{lockId}/paid";
	public static final String LOCK_CATEGORY_ENDPOINT = "/category";
	public static final String OS_NAME = "os.name";
	public static final String WIN = "win";
	public static final String EXCEPTION_SAVING_FILES = "Exception saving file : ";
	public static final String LOCK_CATEGORY_NOT_FOUND = "Lock category not found";
	public static final String LOCK_CATEGORY_TEMPLATE_NOT_FOUND = "Lock template not found";
	public static final String LOCK_CATEGORY_FILE_ENDPOINT = "/category/file";
	public static final String LOCK_TEMPLATE_FILE_ENDPOINT = "/template/file";
	public static final String LOCK_GLITTERING_FILE_ENDPOINT = "/glittering/file";
	public static final String BRIDGE_ENDPOINT = "/bridge/file";
	public static final String BRIDGE_VIDEO_ENDPOINT = "/bridge/video";
	public static final String ADMIN_AUTHORITY = "ADMIN";
	public static final String FILE_ENDPOINT = "file";
	public static final String FILE_NOT_FOUND = "File not found!";
	public static final String EXCEPTION_DOWNLOADING_THE_FILE = "Exception downloading the file";
	public static final String DOWNLOAD = "/download";
	public static final String LOCK_SECTION_ENDPOINT = "/section";
	public static final String LOCK_DELETE_USING_PASSWORD = "/password";
	public static final String LOCK_SECTION_NOT_FOUND = "File section not found!";
	public static final String FACEBOOK_ENDPOINT = "/facebook";
	public static final String FACEBOOK_CLIENT_ID = "facebook.clientId";
	public static final String FACEBOOK_CLIENT_SECRET = "facebook.clientSecret";
	public static final String HTTPS_GRAPH_FACEBOOK_COM_OAUTH_ACCESS_TOKEN = "https://graph.facebook.com/oauth/access_token";
	public static final String GRANT_TYPE = "grant_type";
	public static final String FB_EXCHANGE_TOKEN = "fb_exchange_token";
	public static final String CLIENT_ID = "client_id";
	public static final String CLIENT_SECRET = "client_secret";
	public static final String DOUBLE_QUOTE = "\"";
	public static final String FACEBOOK_ME = "me";
	public static final String FACEBOOK_ID = "id";
	public static final String FACEBOOK_EMAIL = "email";
	public static final String FACEBOOK_FIRST_NAME = "first_name";
	public static final String FACEBOOK_LAST_NAME = "last_name";
	public static final String FACEBOOK_HOMETOWN = "hometown";
	public static final String LOCATION = "location";
	public static final String CHANGE_PASSWORD = "/change/password";
	public static final String OLD_PASSWORD_DOES_NOT_MATCH = "Old password doesn't match!";
	public static final String PANEL_ENDPOINT = "panel";
	public static final String BRIDGE_LOCKS = "download/file/bridgeLocks";
	public static final String PANEL_NOT_FOUND = "Panel not found!";
	public static final String RANDOM_PANEL = "/random";
	public static final String SOMETHING_WENT_WRONG_WITH_PANELS_COUNTING = "Something went wrong with panels counting!";
	public static final String MAINTAINING_PANELS = "Maintaining panels.";
	public static final String EMAIL_NOT_CONFIRMED = "Email not confirmed.";
	public static final String LOCK_DELETE_USING_TOKEN = "/token";
	public static final String LOCK_NOT_FOUND = "Lock not found!";
	public static final String USER = "/user";
	public static final String RANDOM_PANEL_PARAM = "randomParameters.randomLocksOnUserPanel";
	public static final String FACEBOOK_FRIENDS = "friends";
	public static final String FAILED_TO_GET_FRIENDS_LIST = "Failed to get friends list";
	public static final String ID = "id";
	public static final String DATA = "data";
	public static final String PANEL_MAX_SIZE = "panel.maxSize";
	public static final String UNAUTHORIZED = "UNAUTHORIZED";
	public static final String FILE_URL_PART = "download/file/";
	public static final String FACEBOOK_LOGOUT_ENDPOINT = "/facebook/logout";

//	public static String lockCategoryPictureUrl(Long categoryId, String domain) {
//		return domain + API + "category/" + categoryId + "/pic";
//	}
//
//	public static String lockTemplatePictureUrl(Long templateId, String domain) {
//		return domain + API + "template/" + templateId + "/pic";
//	}
//
//	public static String lockPartiallyErasedTemplatePictureUrl(Long templateId, String domain) {
//		return domain + API + "partialTemplate/" + templateId + "/pic";
//	}
//
//	public static String lockPartiallyErasedTemplateWithTextPictureUrl(Long lockId, String domain) {
//		return domain + API + "partialTemplateWithText/" + lockId + "/pic";
//	}
//
//	public static String lockBridgePictureUrl(String domain,Long fileId) {
//		return domain + API + "bridge/" + fileId + "/pic";
//	}

	public static String downloadFileUrl(Long fileId, String domain) {
		return domain
				+ API
				+ FILE_URL_PART
				+ fileId;
	}

	public static String getFileKey(String fileName, binar.box.domain.File.Type type, Long fileTypeId) {
		return type + File.separator + fileTypeId + "_" + fileName;
	}
}
