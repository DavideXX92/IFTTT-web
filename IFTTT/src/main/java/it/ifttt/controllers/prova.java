package it.ifttt.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.FilterStreamParameters;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.UserStreamParameters;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import it.ifttt.domain.Action;
import it.ifttt.domain.Channel;
import it.ifttt.domain.Recipe;
import it.ifttt.domain.Trigger;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;
import it.ifttt.repository.ChannelRepository;
import it.ifttt.repository.UserRepository;
import it.ifttt.services.RepoService;
import it.ifttt.services.security.SecurityService;
import it.ifttt.social.CalendarCreator;
import it.ifttt.social.TwitterTemplateCreator;

@RestController
@RequestMapping("/prova")
public class prova {
	public static final String SUMMARY_KEY = "summary";
	public static final String DESCRIPTION_KEY = "description";
	public static final String LOCATION_KEY = "location";
	public static final String CREATOR_KEY = "creator";	
	public static final String ATTENDEES_KEY = "attendees";
	public static final String CREATED_DATE_KEY = "created-date";
	public static final String CREATOR_NAME_KEY = "creator-name";
	public static final String START_DATE_KEY = "start-date";
	public static final String END_DATE_KEY = "end-date";
	
	public static final String ALL_DAY_KEY = "all-day";
	public static final String START_KEY = "start";
	public static final String END_KEY = "end";
	public static final String TIME_ZONE_KEY = "time-zone";
	public static final String RECURRENT_KEY = "recurrent";
	public static final String FREQUENCY_KEY = "frequency";
	public static final String COUNT_KEY = "count";
	public static final String UNTIL_KEY = "until";	
	public static final String REMINDER_KEY = "reminder";
	public static final String REMINDER_METHOD_KEY = "reminder-method";
	public static final String REMINDER_MINUTES_KEY = "reminder-minutes";
	
	public static final String FREQUENCY_HOURLY = "HOURLY";
	public static final String FREQUENCY_DAILY = "DAILY";
	public static final String FREQUENCY_WEEKLY = "WEEKLY";
	public static final String FREQUENCY_MONTHLY = "MONTHLY";
	public static final String FREQUENCY_YEARLY = "YEARLY";
	
	public static final String REMINDER_POPUP = "popup";
	public static final String REMINDER_EMAIL = "email";	
	public static final String FROM_KEY = "from";
	public static final String TO_KEY = "to";
	public static final String CC_KEY = "cc";
	public static final String SUBJECT_KEY = "subject";
	public static final String LABELS_KEY = "labels";
	public static final String BODY_KEY = "body";
	
	public static final String FROM_NAME_KEY = "from-name";
	public static final String TO_NAME_KEY = "to-name";
	public static final String THREAD_ID_KEY = "thread-id";
	
	public static final String CHANNEL = "gmail";
	
	@Autowired
    private SecurityService securityService;
	
	@Autowired
	private UserRepository UserRepository;
	
	@Autowired
	private ChannelRepository channelRepository;
	
	@Autowired
	private TwitterTemplateCreator twitterTemplateCreator;
	
	@Autowired
	private RepoService repoService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getRecipe() {				
		return "It works!";
	}
	
	@RequestMapping(value="/getChannels", method = RequestMethod.GET)
    public Set<Channel> getChannels() {
		return repoService.getChannels();
	}
	
	@RequestMapping(value="/getIngredients", method = RequestMethod.GET)
    public List<UserIngredient> getIngredients() {
		int idU = 1;
		int idR = 1;
		return repoService.getIngredients(idU, idR);
	}
	
	
	@RequestMapping(value="/popolate", method = RequestMethod.PUT)
    public String popola() {	
		String gmail = "gmail";
		String calendar = "gcalendar";
		String meteo = "weather";
		List<String> gmailActionName = new ArrayList<>(Arrays.asList("SEND_EMAIL"));
		List<String> gmailTriggerName = new ArrayList<>(Arrays.asList("MAIL_RECEIVED"));
		List<String> gcalendarActionName = new ArrayList<>(Arrays.asList("CREATE_CALENDAR_EVENT"));
		List<String> gcalendarTriggerName = new ArrayList<>(Arrays.asList("CALENDAR_EVENT_STARTED","CALENDAR_EVENT_CREATED"));
		List<String> meteoTriggerName = new ArrayList<>(Arrays.asList("SUNRISE","CURRENT_TEMPERATURE_BELOW_TH","CURRENT_TEMPERATURE_ABOVE_TH",
				"TOMORROW_WEATHER"));
		List<String> gmailActionIngredient = new ArrayList<>(Arrays.asList(FROM_KEY, TO_KEY, CC_KEY, SUBJECT_KEY, LABELS_KEY, BODY_KEY, FROM_NAME_KEY,
				TO_NAME_KEY, THREAD_ID_KEY));
		List<String> gcalendarActionIngredient = new ArrayList<>(Arrays.asList(SUMMARY_KEY,DESCRIPTION_KEY,LOCATION_KEY,CREATOR_KEY,ATTENDEES_KEY,CREATED_DATE_KEY,
				CREATOR_NAME_KEY,START_DATE_KEY,END_DATE_KEY,ALL_DAY_KEY,START_KEY,END_KEY,
				TIME_ZONE_KEY,RECURRENT_KEY,FREQUENCY_KEY,COUNT_KEY,UNTIL_KEY,REMINDER_KEY,
				REMINDER_METHOD_KEY,REMINDER_MINUTES_KEY,FREQUENCY_HOURLY,FREQUENCY_DAILY,
				FREQUENCY_WEEKLY,FREQUENCY_MONTHLY,FREQUENCY_MONTHLY,REMINDER_POPUP,REMINDER_EMAIL));
		List<String> gmailTriggerIngredient = new ArrayList<>(Arrays.asList(FROM_KEY, TO_KEY, CC_KEY, SUBJECT_KEY, LABELS_KEY, BODY_KEY));
		List<String> gcalendarTriggerIngredient = new ArrayList<>(Arrays.asList(SUMMARY_KEY,DESCRIPTION_KEY,LOCATION_KEY,CREATOR_KEY));		
		
		Channel gmailCh = new Channel();
		gmailCh.setNameCh(gmail);
		List<Trigger> gmailTr = new ArrayList<Trigger>();
		for(String tmp : gmailTriggerName){
			Trigger t = new Trigger();
			t.setNameT(tmp);
			gmailTr.add(t);
		}		
		gmailCh.setTriggers(gmailTr);
		List<Action> gmailA = new ArrayList<Action>();
		for(String tmp : gmailActionName){
			Action t = new Action();
			t.setNameA(tmp);
			
			gmailA.add(t);
		}
		gmailCh.setActions(gmailA);
		gmailCh = channelRepository.save(gmailCh);
		
		Channel calendarCh = new Channel();
		calendarCh.setNameCh(calendar);
		List<Trigger> calendarTr = new ArrayList<Trigger>();
		for(String tmp : gcalendarTriggerName){
			Trigger t = new Trigger();
			t.setNameT(tmp);
			calendarTr.add(t);
		}
		calendarCh.setTriggers(calendarTr);
		List<Action> calendarA = new ArrayList<Action>();
		for(String tmp : gcalendarActionName){
			Action t = new Action();
			t.setNameA(tmp);
			calendarA.add(t);
		}
		calendarCh.setActions(calendarA);
		calendarCh = channelRepository.save(calendarCh);
		
		return "popolato";
	}
	
	@RequestMapping(value="/getRecipes", method = RequestMethod.GET)
    public List<Recipe> getRecipes() {
		return repoService.getRecipes();
	}
	
	@RequestMapping(value="/getRecipe", method = RequestMethod.GET)
    public Recipe getRecipeCtrl() {
		return repoService.getRecipe("chT1idT1chA2idA1");
	}
	
	@CrossOrigin
	@RequestMapping(value="/saveRecipe", method = RequestMethod.POST)
    public Recipe saveRecipe(@RequestBody Recipe recipe) {
		System.out.println("received: saveRecipe");
		recipe.generateControl();
		return repoService.saveRecipe(recipe);
	}
	
	@CrossOrigin
	@RequestMapping(value="/saveUserIngredients", method = RequestMethod.POST)
    public void saveRecipe(@RequestBody List<UserIngredient> userIngredientList) {
		Iterator <UserIngredient> it = userIngredientList.iterator();
		while(it.hasNext()) {
			UserIngredient userIngredient = it.next();
			repoService.saveUserIngredients(userIngredient);
		}
	}
	
	@RequestMapping(value="/getUsersWithAtLeastArecipeActive", method = RequestMethod.GET)
    public List<User> getUsersWithAtLeastArecipeActive() {
		int idR = 1;
		return repoService.getUsersWithAtLeastArecipeActive(idR);
	}
	
	@RequestMapping(value="/printChannels", method = RequestMethod.GET)
    public String printChannels() {
	Set<Channel> channels = repoService.getChannels();
		Iterator <Channel> it = channels.iterator();
        String str = "Channels: <br>\n";
		while(it.hasNext()) {
                Channel ch = it.next();
                str += "<br>\n&nbsp;&nbsp;&nbsp;";
                str += "name: " + ch.getNameCh() + "<br>\n";
                
                List<Trigger> triggers = ch.getTriggers();
                Iterator<Trigger> itTr = triggers.iterator();
                str += "&nbsp;&nbsp;&nbsp;";
                str += "Triggers: <br>\n";
                while(itTr.hasNext()){
                	Trigger tr = itTr.next();
                	str += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                	str +=  "- " + tr.getNameT() + "<br>\n";
                }
                
                List<Action> actions = ch.getActions();
                Iterator<Action> itAct = actions.iterator();
                str += "&nbsp;&nbsp;&nbsp;";
                str += "Actions: <br>\n";
                while(itAct.hasNext()){
                	Action act = itAct.next();
                	str += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                	str +=  "- " + act.getNameA() + "<br>\n";
                }
        }
		return str;
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