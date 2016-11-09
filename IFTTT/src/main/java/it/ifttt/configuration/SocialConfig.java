package it.ifttt.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
@EnableSocial
@ComponentScan(basePackages = {"it.ifttt.configuration"})
public class SocialConfig implements SocialConfigurer {
 /*
	@Autowired
	private AccountConnectionSignUpService accountConnectionSignUpService;
	*/ 
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private Environment environment;
	 
	
	@Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new TwitterConnectionFactory(
        		environment.getProperty("twitter.appKey"),
        		environment.getProperty("twitter.appSecret")));
        registry.addConnectionFactory(new GoogleConnectionFactory(
        		environment.getProperty("google.clientId"),
				environment.getProperty("google.clientSecret")));
        registry.addConnectionFactory(new FacebookConnectionFactory(
        		environment.getProperty("facebook.clientId"),
        		environment.getProperty("facebook.clientSecret")));
        		  
        	
        return registry;
	}
	
	
	
	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfc, Environment env) {
	  
	cfc.addConnectionFactory(new TwitterConnectionFactory(
		env.getProperty("twitter.appKey"),
		env.getProperty("twitter.appSecret")));
	cfc.addConnectionFactory(new GoogleConnectionFactory(
    		environment.getProperty("google.clientId"),
			environment.getProperty("google.clientSecret")));
		  /*
		  GoogleConnectionFactory gcf = new GoogleConnectionFactory(
		   env.getProperty("spring.social.google.appId"),
		   env.getProperty("spring.social.google.appSecret"));
		  gcf.setScope("email");
		  cfc.addConnectionFactory(gcf);*/
	}
	 
	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	} 
	
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator cfl) {
		 // questo dovrebbe essere quello che in qualche modo riempie la tabella UserConnection
		 JdbcUsersConnectionRepository repository =
				 new JdbcUsersConnectionRepository(dataSource, cfl, Encryptors.noOpText());
		 //repository.setConnectionSignUp(accountConnectionSignUpService);
		 return repository;
	}
	
	@Bean
	@Scope(value="prototype", proxyMode=ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
        }
        return getUsersConnectionRepository(connectionFactoryLocator()).createConnectionRepository(authentication.getName());
    }
	/*
	@Bean
    public ConnectController connectController() {
        return new ConnectController(connectionFactoryLocator(), connectionRepository());
    }
    */
}
