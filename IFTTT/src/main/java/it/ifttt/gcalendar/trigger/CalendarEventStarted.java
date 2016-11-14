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

import org.springframework.beans.factory.annotation.Autowired;

import it.ifttt.domain.TriggerRefresh;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;
import it.ifttt.repository.TriggerRefreshRepository;
import it.ifttt.social.CalendarCreator;
import it.ifttt.trigger.TriggerEvent;
import com.google.api.services.calendar.Calendar;

public class CalendarEventStarted implements TriggerEvent{
	@Autowired
	CalendarCreator calendarCreator;
	
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
	
	public CalendarEventStarted(){
		this.user = null;
		this.userIngredients = null;
	}
	
	@Override
	public List<Object> raise(User user, List<UserIngredient> ingredients) throws IOException{
		List<Object> eventStarted = new ArrayList<Object>();
		try {			
			// get last created events
			DateTime now = new DateTime(System.currentTimeMillis());
	        Events events = calendar.events().list("primary")
	            .setTimeMin(now)
	            .setOrderBy("updated")
	            .setSingleEvents(false)
	            .execute();
	        List<Event> items = events.getItems();
	        if (items.size() == 0)
	            return eventStarted;
	        
	        for (Event event : items) {
	        	
	        	System.out.printf("Event: %s (%s)\n", event.getSummary(), event.getCreated());
	        	
	        	if (new Date(event.getCreated().getValue()).after(triggerRefresh.getLastRefresh())) {
	        		
	        		System.out.println("Is new event");
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
			String name = userIngredient.getIngredient().getNameIngr();
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
