package it.ifttt.gcalendar.action;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import it.ifttt.action.ActionPerformer;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;
import it.ifttt.domain.UserIngredientPK;
import it.ifttt.repository.IngredientRepository;
import it.ifttt.social.CalendarCreator;

@Component
public class CalendarCreateEvent implements ActionPerformer{
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
	
	@Autowired
	private CalendarCreator calendarCreator;
	
	@Autowired
	private IngredientRepository ingredientRepository;
	
	private User user;
	private List<UserIngredient> actionIngredient;
	private List<UserIngredient> injectedIngredients;
	private Calendar calendar;	
	
	@Override
	public void perform() {
		// main ingredients
		String summary = resolveIngredient(SUMMARY_KEY, true);
		String location = resolveIngredient(LOCATION_KEY, true);
		String description = resolveIngredient(DESCRIPTION_KEY, true);		
		String timeZone = resolveIngredient(TIME_ZONE_KEY, false);
		boolean allDay = Boolean.parseBoolean(resolveIngredient(ALL_DAY_KEY, false));
		Date start = new Date(Long.parseLong(resolveIngredient(START_KEY,false)));
		Date end = new Date(Long.parseLong(resolveIngredient(END_KEY,false)));
		boolean recurrent = Boolean.parseBoolean(resolveIngredient(RECURRENT_KEY, false));
		String frequency = null;
		int count = 0;
		Date until = null;
		String[] attendees = null;
		boolean reminder;
		String reminderMethod = null;
		int reminderMinutes = 0;
		
		if (recurrent) {
			frequency = resolveIngredient(FREQUENCY_KEY, false);
			if (resolveIngredient(COUNT_KEY, true) != null)
				count = Integer.parseInt(resolveIngredient(COUNT_KEY, true));
			else if (resolveIngredient(UNTIL_KEY, true) != null)
				until = new Date(Long.parseLong(resolveIngredient(UNTIL_KEY, true)));
		}
		attendees = resolveIngredient(ATTENDEES_KEY, false).split(" ");
		reminder = Boolean.parseBoolean(resolveIngredient(REMINDER_KEY, false));
		if (reminder) {
			reminderMethod = resolveIngredient(REMINDER_METHOD_KEY, false);
			reminderMinutes = Integer.parseInt(resolveIngredient(REMINDER_MINUTES_KEY, false));
		}
		
		try {						
			Event event = buildEvent(summary, location, description, allDay, timeZone, start, end, 
					recurrent, frequency, count, until, attendees, reminder, reminderMethod, reminderMinutes);
			
			event = calendar.events().insert("primary", event).execute();
			System.out.printf("Event created: %s\n", event.getHtmlLink());
			System.out.println("Performed action 'create-event' of channel Calendar.");
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Event buildEvent(String summary, String location, String description, boolean allDay, String timeZone, 
			Date start, Date end, boolean recurrent, String frequency, int count, Date until, String[] attendees,
			boolean reminder, String reminderMethod, int reminderMinutes) {
		Event event = new Event()
		    .setSummary(summary)
		    .setLocation(location)
		    .setDescription(description);

		
		
		if (allDay) {
			DateTime startDateTime = new DateTime(start);
			
			end = new Date(startDateTime.getValue() + 86400000);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    String startDateStr = dateFormat.format(start);
		    String endDateStr = dateFormat.format(end);
		    startDateTime = new DateTime(startDateStr);
		    DateTime endDateTime = new DateTime(endDateStr);
		    EventDateTime startEventDateTime = new EventDateTime().setDate(startDateTime);
		    EventDateTime endEventDateTime = new EventDateTime().setDate(endDateTime);
		    event.setStart(startEventDateTime);
		    event.setEnd(endEventDateTime);
		    
		} else {
			DateTime startDateTime = new DateTime(start);
			
			EventDateTime startEventDateTime = new EventDateTime()
				    .setDateTime(startDateTime)
				    .setTimeZone(timeZone);
			event.setStart(startEventDateTime);
			
			DateTime endDateTime = new DateTime(end);
			EventDateTime endEventDateTime = new EventDateTime()
			    .setDateTime(endDateTime)
			    .setTimeZone(timeZone);
			event.setEnd(endEventDateTime);
		}

		if (recurrent) {
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
			
			String recurrence = "RRULE:FREQ=" + frequency + ";";
			if (until != null)
				recurrence += "UNTIL=" + dateFormat.format(until);
			else if (count != 0)
				recurrence += "COUNT=" + count;
			System.out.println(recurrence);
			String[] recurrences = new String[] {recurrence};
			event.setRecurrence(Arrays.asList(recurrences));
		}

		if (attendees != null) {
			List<EventAttendee> attendeesList = new ArrayList<>();
			for (String attendee : attendees) {
				System.out.println(attendee);				
				attendeesList.add(new EventAttendee().setEmail(attendee));
			}
			event.setAttendees(attendeesList);
		}
		
		if (reminder) {
			EventReminder[] reminderOverrides = new EventReminder[] {
				    new EventReminder().setMethod(reminderMethod).setMinutes(reminderMinutes)
			};
			Event.Reminders reminders = new Event.Reminders()
			    .setUseDefault(false)
			    .setOverrides(Arrays.asList(reminderOverrides));
			event.setReminders(reminders);
		}
		return event;
	}
	
	@Override
	public void setUser(User user) throws IOException, GeneralSecurityException {
		this.user = user;
		this.calendar = calendarCreator.getCalendar(user.getUsername());	
	}

	@Override
	public void setActionIngredients(List<UserIngredient> userIngredients) {
		this.actionIngredient = userIngredients;
	}

	private String resolveIngredient(String name, boolean nullable){
		UserIngredient tmp = null;
		for(UserIngredient ui : actionIngredient){
			int ingredientId = ui.getIdUI().idIngr;
			if(ingredientRepository.findOne(ingredientId).getNameIngr().equals(name)){
				tmp = ui;
				break;
			}							
		}
		
		if( nullable || (tmp.getValue() != null) )
			return tmp.getValue(); //SOSTITUIRE CON FUNZIONE CHE RICERCA TAG
		
		
		tmp = null;
		for(UserIngredient ui : injectedIngredients){
			int ingredientId = ui.getIdUI().idIngr;
			if(ingredientRepository.findOne(ingredientId).getNameIngr().equals(name)){
				tmp = ui;
			}							
		}
		
		return tmp.getValue();
	}
	
	@Override
	public void injectIngredients(List<Ingredient> injeactableIngredient,Object obj) {
		Message message = (Message)obj;
		injectedIngredients = new ArrayList<UserIngredient>();
		
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
	}
	
	private UserIngredient newUserIngredient(Ingredient ingredient, String value){
		UserIngredient userIngredient = new UserIngredient();
		userIngredient.setValue(value);
		UserIngredientPK uipk = new UserIngredientPK();
		uipk.idIngr = ingredient.getIdIngr();
		userIngredient.setIdUI(uipk);;
		
		return userIngredient;
	}
}
