package it.ifttt.gcalendar.trigger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.gmail.model.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ifttt.domain.Ingredient;
import it.ifttt.domain.TriggerRefresh;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;
import it.ifttt.domain.UserIngredientPK;
import it.ifttt.repository.IngredientRepository;
import it.ifttt.repository.TriggerRefreshRepository;
import it.ifttt.social.CalendarCreator;
import it.ifttt.trigger.TriggerEvent;
import com.google.api.services.calendar.Calendar;

@Component
public class CalendarEventStarted implements TriggerEvent{
	@Autowired
	CalendarCreator calendarCreator;
	
	@Autowired
	TriggerRefreshRepository triggerRefreshRepository;
	@Autowired
	private IngredientRepository ingredientRepository;
	
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
	
	public static final String CHANNEL = "gcalendar";
	
	private User user;
	private List<UserIngredient> userIngredients;
	private Calendar calendar;
	
	private TriggerRefresh triggerRefresh;
	
	@Override
	public List<Object> raise(User user, List<UserIngredient> ingredients) throws IOException{
		List<Object> eventStarted = new ArrayList<Object>();
		try {			
			DateTime lastRefresh = new DateTime(triggerRefresh.getLastRefresh());
	        Events events = calendar.events().list("primary")
	            .setTimeMin(lastRefresh)
	            .setOrderBy("updated")
	            .setSingleEvents(false)
	            .execute();
	        List<Event> items = events.getItems();
	        if (items.size() == 0)	        	
	            return eventStarted;
	        
	        for (Event event : items) {
	        	if(triggerRefresh.getLastRefresh().after(new Date(event.getCreated().getValue())))
		        	if(new Date(event.getStart().getDateTime().getValue()).after(triggerRefresh.getLastRefresh())){		        		
		        		System.out.println("Started event!");
		        		//setLastCheck(new Date(event.getCreated().getValue()));
		        		if (eventSatisfyTrigger(event)) {
		        			System.out.println("Event satisfy trigger");
		        			eventStarted.add(event);						
		        		}
		        	}
	        }
	        
	        if(eventStarted.size() > 0){
	        	triggerRefresh.setLastRefresh(new Date());
	        	triggerRefreshRepository.save(triggerRefresh);
	        }
	        
	        return eventStarted;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return eventStarted;
	}

	@Override
	public void setUser(User user) throws IOException, GeneralSecurityException {
		this.user = user;
		calendar = calendarCreator.getCalendar(user.getUsername());		
	}

	@Override
	public void setIngredients(List<UserIngredient> userIngredients) {
		this.userIngredients = userIngredients;
	}
	
	private boolean eventSatisfyTrigger(Event event) {
		for(UserIngredient userIngredient : userIngredients){
			Ingredient i = ingredientRepository.findOne(userIngredient.getIdUI().getIdIngr());
			String name = i.getNameIngr();
			switch(name){
				case SUMMARY_KEY:
					if(!userIngredient.getValue().equals(event.get(SUMMARY_KEY)))
						return false;					
					break;
				case DESCRIPTION_KEY:
					if(!userIngredient.getValue().equals(event.get(DESCRIPTION_KEY)))
						return false;
					break;
				case LOCATION_KEY:
					if(!userIngredient.getValue().equals(event.get(LOCATION_KEY)))
						return false;
					break;
				case CREATOR_KEY:
					if(!userIngredient.getValue().equals(event.get(CREATOR_KEY)))
						return false;
					break;
			}
		}		
		
		return true;
	}

	@Override
	public void setTriggerRefresh(TriggerRefresh triggerRefresh) {
		this.triggerRefresh = triggerRefresh;
	}
	
	@Override
	public List<UserIngredient> injectIngredients(List<Ingredient> injeactableIngredient,Object obj) {
		List<UserIngredient> injectedIngredients = new ArrayList<UserIngredient>();
		Message message = (Message)obj;
		
		for(Ingredient i : injeactableIngredient){
			switch(i.getNameIngr()){
			case SUMMARY_KEY:
				injectedIngredients.add(newUserIngredient(i, (String) message.get(SUMMARY_KEY)));
				break;
			case DESCRIPTION_KEY:
				injectedIngredients.add(newUserIngredient(i, (String) message.get(DESCRIPTION_KEY)));
				break;
			case LOCATION_KEY:
				injectedIngredients.add(newUserIngredient(i, (String) message.get(LOCATION_KEY)));
				break;
			case CREATOR_KEY:
				injectedIngredients.add(newUserIngredient(i, (String) message.get(CREATOR_KEY)));
				break;
			case ATTENDEES_KEY:
				injectedIngredients.add(newUserIngredient(i, (String) message.get(ATTENDEES_KEY)));
				break;
			case CREATED_DATE_KEY:
				injectedIngredients.add(newUserIngredient(i, (String) message.get(CREATED_DATE_KEY)));
				break;
			case CREATOR_NAME_KEY:
				injectedIngredients.add(newUserIngredient(i, (String) message.get(CREATOR_NAME_KEY)));
				break;
			case START_DATE_KEY:
				injectedIngredients.add(newUserIngredient(i, (String) message.get(START_DATE_KEY)));
				break;
			case END_DATE_KEY:
				injectedIngredients.add(newUserIngredient(i, (String) message.get(END_DATE_KEY)));
				break;
			}
		}		
		
		return injectedIngredients;
	}
	
	private UserIngredient newUserIngredient(Ingredient ingredient, String value){
		UserIngredient userIngredient = new UserIngredient();
		userIngredient.setValue(value);
		UserIngredientPK uipk = new UserIngredientPK();
		uipk.setIdIngr(ingredient.getIdIngr());
		userIngredient.setIdUI(uipk);;
		
		return userIngredient;
	}
}
