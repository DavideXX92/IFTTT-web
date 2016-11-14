 package it.ifttt.social;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.google.api.Google;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;

import it.ifttt.domain.UserConnection;
import it.ifttt.repository.UserConnectionRepository;
import it.ifttt.security.UnauthorizedChannelException;

@Component
public class CalendarCreator {

	public static final String GOOGLE_ID = "google";
	
	@Autowired
	private Environment environment;

	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	private UserConnectionRepository userConnectionRepository;
	
	public Calendar getCalendar(String username) throws GeneralSecurityException, IOException {
		
		UserConnection userConnection = userConnectionRepository.findOneByUserIdAndProviderId(
				username,
				GOOGLE_ID
		);
		if (userConnection == null)
			throw new UnauthorizedChannelException("No authorization found for user '" + username + "' on channel '" + GOOGLE_ID + "'.");
		
		checkAndRefreshConnection(userConnection);
		userConnection = userConnectionRepository.findOneByUserIdAndProviderId(
				username,
				GOOGLE_ID
		);
		
		String accessToken = userConnection.getAccessToken();
		String refreshToken = userConnection.getRefreshToken();
		String clientId = environment.getProperty("google.clientId");
		String clientSecret = environment.getProperty("google.clientSecret");
		
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setAccessToken(accessToken);
		tokenResponse.setRefreshToken(refreshToken);
		
		// TODO l'access token che c'è nel db dopo due ore scade, in qualche modo dovrebbe essere possibile rigenerarlo con il refreshToken
		Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
			.setTransport(GoogleNetHttpTransport.newTrustedTransport())
			.setJsonFactory(JacksonFactory.getDefaultInstance())
			.setTokenServerUrl(
		            new GenericUrl("http://127.0.0.1:8080/IFTTT/"))
			.setClientAuthentication(new BasicAuthentication(clientId, clientSecret))
			.build()
			.setFromTokenResponse(tokenResponse);
		
		Calendar calendar = new Calendar.Builder(
				GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(),
				credential
		)
            .setApplicationName("progetto-ai")
            .build();
	
		return calendar;
	}
	
	private void checkAndRefreshConnection(UserConnection userConnection) {
		
		// force authentication to this user to get it's google connection through spring social
		Authentication authentication = new UsernamePasswordAuthenticationToken(userConnection.getUserId(), "", null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		ConnectionRepository connectionRepository = appContext.getBean(ConnectionRepository.class);
		
		Connection<Google> connection = connectionRepository.getPrimaryConnection(Google.class);
		
		if (connection.hasExpired()) {
			
			// save the refresh token (it will became null after refreshing for some spring social mis-function, but it will be still good) 
			String refreshToken = userConnection.getRefreshToken();
			
			// refresh the connection
			connection.refresh();
			
			// store new access token
			connectionRepository.updateConnection(connection);
			
			// save the old still good refresh token
			userConnection =  userConnectionRepository.findOneByUserIdAndProviderId(userConnection.getUserId(), "google");
			userConnection.setRefreshToken(refreshToken);
			userConnectionRepository.save(userConnection);
		}
		
	}
}
