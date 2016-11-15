package it.ifttt.gcalendar.trigger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.Events;

import it.ifttt.domain.Ingredient;
import it.ifttt.domain.TriggerRefresh;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;
import it.ifttt.repository.IngredientRepository;
import it.ifttt.repository.TriggerRefreshRepository;
import it.ifttt.social.CalendarCreator;
import it.ifttt.trigger.TriggerEvent;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;

public class CalendarEventCreated implements TriggerEvent{
	@Autowired
	CalendarCreator calendarCreator;
	@Autowired
	private IngredientRepository ingredientRepository;
	
	@Autowired
	TriggerRefreshRepository triggerRefreshRepository;
	
	public static final String SUMMARY_KEY = "summary";
	public static final String DESCRIPTION_KEY = "description";
	public static final String LOCATION_KEY = "location";
	public static final String CREATOR_KEY = "creator";	
	
	public static final String CHANNEL = "gmail";
	
	private User user;
	private List<UserIngredient> userIngredients;
	private Calendar calendar;
	
	private TriggerRefresh triggerRefresh;
	
	public CalendarEventCreated(){
		this.user = null;
		this.userIngredients = null;
	}
	
	@Override
	public List<Object> raise(User user, List<UserIngredient> ingredients) {	
		List<Object> satisfiedEvents = new ArrayList<Object>();
		try {
			DateTime lastCheck = new DateTime(triggerRefresh.getLastRefresh());
			DateTime now = new DateTime(System.currentTimeMillis()+10*1000);
			
	        // get next events for today
	        Events events = calendar.events().list("primary")
	            .setTimeMin(lastCheck)
	            .setTimeMax(now)
	            .setOrderBy("startTime")
	            .setSingleEvents(true)
	            .execute();
	        List<Event> items = events.getItems();			
	        for (Event event : items) {
        		if (eventSatisfyTrigger(event)) {
        			System.out.println("Event satisfy trigger");
        			satisfiedEvents.add(event);
        		}
	        }
	        return satisfiedEvents;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(satisfiedEvents.size()>0){
			triggerRefresh.setLastRefresh(new Date());
			triggerRefreshRepository.save(triggerRefresh);
		}
		
		return satisfiedEvents;
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
			Ingredient i = ingredientRepository.findOne(userIngredient.getIdUI().idIngr);
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
}
