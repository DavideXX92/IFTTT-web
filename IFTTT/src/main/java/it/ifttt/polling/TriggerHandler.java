package it.ifttt.polling;

import it.ifttt.domain.Recipe;
import it.ifttt.domain.Trigger;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;
import it.ifttt.gcalendar.trigger.CalendarEventCreated;
import it.ifttt.gcalendar.trigger.CalendarEventStarted;
import it.ifttt.gmail.trigger.mailReceivedEvent;
import it.ifttt.repository.TriggerRefreshRepository;
import it.ifttt.trigger.TriggerEvent;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

public class TriggerHandler {
	private Trigger trigger;
	private TriggerEvent event;	
		
	@Autowired
	private TriggerRefreshRepository triggerRefreshRepository;
	
	public TriggerHandler(Trigger trigger){
		this.trigger = trigger;
	}
	
	public List<Object> raise(User user, List<UserIngredient> ingredients, Recipe recipe)throws IOException{
		try{
			event.setUser(user);
		}catch(Exception e){
			System.out.println("Errore: " + e.getMessage());
			return new ArrayList<Object>();
		}
		event.setIngredients(ingredients);
		event.setTriggerRefresh(triggerRefreshRepository.findOne(user.getId(), recipe.getIdR(), trigger.getIdT().idT, trigger.getIdT().idCh));
		return event.raise(user, ingredients);
	}
	
	private TriggerEvent setEvent(String triggerName){
		TriggerEvent event = null;
		
		switch(triggerName){
			case "MAIL_RECEIVED":
				event = new mailReceivedEvent();
				break;
			case "EVENT_STARTED":
				event = new CalendarEventStarted();
				break;
			case "EVENT_CREATED":
				event = new CalendarEventCreated();
				break;
			case "SUNRISE":
				
				break;
			case "CURRENT_TEMPERATURE_BELOW_TH":
				
				break;
			case "CURRENT_TEMPERATURE_ABOVE_TH":
				
				break;
			case "TOMORROW_WEATHER":
				
				break;
			default:
		}		
		return event;
	}
}
