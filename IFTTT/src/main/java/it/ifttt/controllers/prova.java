package it.ifttt.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.FilterStreamParameters;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.UserStreamParameters;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

import it.ifttt.model.user.User;
import it.ifttt.repository.UserRepository;
import it.ifttt.services.security.SecurityService;
import it.ifttt.social.CalendarCreator;
import it.ifttt.social.TwitterTemplateCreator;

@RestController
@RequestMapping("/prova")
public class prova {
	
	@Autowired
    private SecurityService securityService;
	
	@Autowired
	private UserRepository UserRepository;
	
	@Autowired
	private TwitterTemplateCreator twitterTemplateCreator;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getRecipe() {				
		return "It works!";
	}
	
	@RequestMapping(value="/user", method = RequestMethod.GET)
    public User currentUser() {
    	User user = UserRepository.findByUsername(securityService.findLoggedInUsername());
    	return user;
    }
	
	@RequestMapping("twitter/notify")
	public String twitterNotify(){
		Twitter twitter = twitterTemplateCreator.getTwitterTemplate("DavideXX92");
		StreamListener listener = new StreamListener() {
			
			@Override
			public void onWarning(StreamWarningEvent warningEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTweet(Tweet tweet) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLimit(int numberOfLimitedTweets) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDelete(StreamDeleteEvent deleteEvent) {
				// TODO Auto-generated method stub
				
			}
		};	
		List<StreamListener> list = new ArrayList<StreamListener>();
		list.add(listener);
		//FilterStreamParameters f = new FilterStreamParameters();
		
		//twitter.streamingOperations().filter(parameters, listeners);
		/*UserStreamListener listener = new UserStreamListener() {
			
			@Override
			public void onException(Exception ex) {
				System.out.println("onException");				
			}
			
			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				// TODO Auto-generated method stub
				System.out.println("onTrackLimitation");
			}
			
			@Override
			public void onStatus(Status status) {
				// TODO Auto-generated method stub
				System.out.println("onStatus");
			}
			
			@Override
			public void onStallWarning(StallWarning warning) {
				// TODO Auto-generated method stub
				System.out.println("onStallWarning");
			}
			
			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				// TODO Auto-generated method stub
				System.out.println("onScrubGeo");
			}
			
			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				// TODO Auto-generated method stub
				System.out.println("onDeletionNotice");
			}
			
			@Override
			public void onUserSuspension(long suspendedUser) {
				// TODO Auto-generated method stub
				System.out.println("onuserSuspension");
			}
			
			@Override
			public void onUserProfileUpdate(twitter4j.User updatedUser) {
				// TODO Auto-generated method stub
				System.out.println("onUserProfileUpdate");
			}
			
			@Override
			public void onUserListUpdate(twitter4j.User listOwner, UserList list) {
				// TODO Auto-generated method stub
				System.out.println("onuserListUpdate");
			}
			
			@Override
			public void onUserListUnsubscription(twitter4j.User subscriber, twitter4j.User listOwner, UserList list) {
				// TODO Auto-generated method stub
				System.out.println("onUserListUnsubscription");
			}
			
			@Override
			public void onUserListSubscription(twitter4j.User subscriber, twitter4j.User listOwner, UserList list) {
				// TODO Auto-generated method stub
				System.out.println("onUserListSubscription");
			}
			
			@Override
			public void onUserListMemberDeletion(twitter4j.User deletedMember, twitter4j.User listOwner, UserList list) {
				// TODO Auto-generated method stub
				System.out.println("onUserListMemberDeletion");
			}
			
			@Override
			public void onUserListMemberAddition(twitter4j.User addedMember, twitter4j.User listOwner, UserList list) {
				// TODO Auto-generated method stub
				System.out.println("onUserListMemberAddition");
			}
			
			@Override
			public void onUserListDeletion(twitter4j.User listOwner, UserList list) {
				// TODO Auto-generated method stub
				System.out.println("onUserListDeletion");
			}
			
			@Override
			public void onUserListCreation(twitter4j.User listOwner, UserList list) {
				// TODO Auto-generated method stub
				System.out.println("onUserListCreation");
			}
			
			@Override
			public void onUserDeletion(long deletedUser) {
				// TODO Auto-generated method stub
				System.out.println("onUserDeletion");
			}
			
			@Override
			public void onUnfollow(twitter4j.User source, twitter4j.User unfollowedUser) {
				// TODO Auto-generated method stub
				System.out.println("onUnfollow");
			}
			
			@Override
			public void onUnfavorite(twitter4j.User source, twitter4j.User target, Status unfavoritedStatus) {
				// TODO Auto-generated method stub
				System.out.println("onUnfavorite");
			}
			
			@Override
			public void onUnblock(twitter4j.User source, twitter4j.User unblockedUser) {
				// TODO Auto-generated method stub
				System.out.println("onUnblock");
			}
			
			@Override
			public void onRetweetedRetweet(twitter4j.User source, twitter4j.User target, Status retweetedStatus) {
				// TODO Auto-generated method stub
				System.out.println("onRetweetedRetweet");
			}
			
			@Override
			public void onQuotedTweet(twitter4j.User source, twitter4j.User target, Status quotingTweet) {
				// TODO Auto-generated method stub
				System.out.println("onQuotedTweet");
			}
			
			@Override
			public void onFriendList(long[] friendIds) {
				// TODO Auto-generated method stub
				System.out.println("onFriendList");
			}
			
			@Override
			public void onFollow(twitter4j.User source, twitter4j.User followedUser) {
				// TODO Auto-generated method stub
				System.out.println("onFollow");
			}
			
			@Override
			public void onFavoritedRetweet(twitter4j.User source, twitter4j.User target, Status favoritedRetweeet) {
				// TODO Auto-generated method stub
				System.out.println("onFavoritedRetweet");
			}
			
			@Override
			public void onFavorite(twitter4j.User source, twitter4j.User target, Status favoritedStatus) {
				// TODO Auto-generated method stub
				System.out.println("onFavorite");
			}
			
			@Override
			public void onDirectMessage(DirectMessage directMessage) {
				// TODO Auto-generated method stub
				System.out.println("onDirectMessage");
			}
			
			@Override
			public void onDeletionNotice(long directMessageId, long userId) {
				// TODO Auto-generated method stub
				System.out.println("onDeletionNotice");
			}
			
			@Override
			public void onBlock(twitter4j.User source, twitter4j.User blockedUser) {
				// TODO Auto-generated method stub
				System.out.println("onBlock");
			}
		};*/
		/*StreamListener listener = new StreamListener() {
	        @Override
	        public void onTweet(Tweet tweet) {
	            System.out.println("new tweet coming in: ");
	        }

	        @Override
	        public void onDelete(StreamDeleteEvent deleteEvent) {
	        	System.out.println("delete event called on stream");
	        }

	        @Override
	        public void onLimit(int numberOfLimitedTweets) {
	        	System.out.println("stream is being track limited");
	        }

	        @Override
	        public void onWarning(StreamWarningEvent warningEvent) {
	        	System.out.println("warning from twitter");
	            }
	        };*/	    	       
	        Stream stream =   twitter.streamingOperations()
	                .filter("filter", Arrays.asList(listener));
	        
	        return "fatto";
	}
}