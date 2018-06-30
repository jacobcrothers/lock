package binar.box.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import binar.box.util.Constants;

/**
 * Created by Timis Nicu Alexandru on 19-Apr-18.
 */
@Configuration
public class FacebookConnectionConfiguration implements SocialConfigurer {

	@Autowired
	private Environment environment;

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer,
			Environment environment) {
		connectionFactoryConfigurer.addConnectionFactory(
				new FacebookConnectionFactory(environment.getProperty(Constants.FACEBOOK_CLIENT_ID),
						environment.getProperty(Constants.FACEBOOK_CLIENT_SECRET)));
	}

	@Override
	public UserIdSource getUserIdSource() {
		return null;
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		return null;
	}
}
