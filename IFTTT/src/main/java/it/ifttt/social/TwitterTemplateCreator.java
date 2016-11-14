package it.ifttt.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

import it.ifttt.domain.UserConnection;
import it.ifttt.repository.UserConnectionRepository;
import it.ifttt.security.UnauthorizedChannelException;

@Component
public class TwitterTemplateCreator {
	
	public static final String TWITTER_ID = "twitter";
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private UserConnectionRepository userConnectionRepository;
 
	public Twitter getTwitterTemplate(String username) {
	   
		UserConnection userConnection = userConnectionRepository.findOneByUserIdAndProviderId(
				username,
				TWITTER_ID
		);
		if (userConnection == null)
			throw new UnauthorizedChannelException("No authorization found for user '" + username + "' on channel '" + TWITTER_ID + "'.");
	   
		String consumerKey = environment.getProperty("twitter.appKey");
		String consumerSecret = environment.getProperty("twitter.appSecret");
		String accessToken = userConnection.getAccessToken();
		String accessTokenSecret = userConnection.getSecret();
	   
		TwitterTemplate twitterTemplate = 
				new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
		return twitterTemplate;
	   
   }
}