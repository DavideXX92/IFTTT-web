package it.ifttt.gmail.trigger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.Profile;

import org.apache.commons.lang3.StringUtils;

import it.ifttt.domain.Action;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.Recipe;
import it.ifttt.domain.TriggerRefresh;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;
import it.ifttt.repository.ActionRepository;
import it.ifttt.repository.RecipeRepository;
import it.ifttt.repository.TriggerRefreshRepository;
import it.ifttt.social.GmailCreator;
import it.ifttt.trigger.TriggerEvent;

public class mailReceivedEvent implements TriggerEvent  {
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
	private TriggerRefreshRepository triggerRefreshRepository;
	
	@Autowired
	private GmailCreator gmailCreator;
	
	private User user;
	private List<UserIngredient> userIngredients;
	private Gmail gmail;
	private TriggerRefresh triggerRefresh;
	
	public mailReceivedEvent(){								
		this.user = null;
		this.userIngredients = null;
	}
	
	public List<Object> raise(User user, List<UserIngredient> ingredients) throws IOException{
		String query = createQueryString();
		ListMessagesResponse listResponse = gmail.users().messages().list("me").setQ(query).execute();
        List<Message> messages = listResponse.getMessages();
        List<Object> newMessages = new ArrayList<Object>();
        
        if (messages == null)
        	return newMessages;
        
        Collections.reverse(messages);
        
        for(Message message : messages){
        	// questo messaggio è solo un header che torna gmail per efficienza, devo prendere quello completo
        	message = gmail.users().messages().get("me", message.getId()).setFormat("raw").execute();
        	
        	try {
				MimeMessage mimeMessage = getMimeMessageFromMessage(message);
				message = fillMessageFieldsFromMimeMessage(message, mimeMessage, gmail.users().getProfile("me").execute());
			} catch (MessagingException e) { continue; }
        	
        	if (new Date(message.getInternalDate()).after(triggerRefresh.getLastRefresh())) {
        		
        		System.out.println("mail id: " + message.getId());
        		System.out.println("Is new mail");        		
        		//setLastCheck(new Date(message.getInternalDate()));
        		if (mailSatisfyTrigger(message)) {        			
        			System.out.println("Mail satisfy trigger");
					newMessages.add(message);
        		}
        	}
        }
        if(newMessages.size() > 0){
        	triggerRefresh.setLastRefresh(new Date());
        	triggerRefreshRepository.save(triggerRefresh);
        }
        
		return newMessages;
	}
	
	public void setUser(User user) throws IOException, GeneralSecurityException{
		this.user = user;
		gmail = gmailCreator.getGmail(user.getUsername());		
	}
	
	public void setIngredients(List<UserIngredient> userIngredients){
		this.userIngredients = userIngredients;
	}
		
	private boolean mailSatisfyTrigger(Message message) {
		for(UserIngredient userIngredient : userIngredients){
			String name = userIngredient.getIngredient().getNameIngr();
			switch(name){
				case FROM_KEY:
					if(!userIngredient.getValue().equals(message.get(FROM_KEY)))
						return false;					
					break;
				case TO_KEY:
					if(!userIngredient.getValue().equals(message.get(TO_KEY)))
						return false;
					break;
				case CC_KEY:
					if(!userIngredient.getValue().equals(message.get(CC_KEY)))
						return false;
					break;
				case SUBJECT_KEY:
					if(!userIngredient.getValue().equals(message.get(SUBJECT_KEY)))
						return false;
					break;
				case LABELS_KEY:
					if(!userIngredient.getValue().equals(message.get(LABELS_KEY)))
						return false;
					break;
				case BODY_KEY:
					if(!userIngredient.getValue().equals(message.get(BODY_KEY)))
						return false;
					break;
			}
		}
		
		return true;
	}
	
	private String createQueryString() {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String query = "";
		/*if (getIngredient(FROM_KEY) != null) {
			query += "from:" + getIngredient(FROM_KEY).getValue();
		}*/
		query += " after:" + dateFormat.format(triggerRefresh.getLastRefresh());
		System.out.println("Query: " + query);
		return query;
	}
	
	private MimeMessage getMimeMessageFromMessage(Message message) throws MessagingException {
		
		byte[] emailBytes = Base64.decodeBase64(message.getRaw());

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));
        
        return email;
	}
	
	private Message fillMessageFieldsFromMimeMessage(Message message, MimeMessage mimeMessage, Profile profile) throws MessagingException {
		
		if (mimeMessage.getHeader("To")[0].contains("<")) {
		message.put(FROM_KEY, StringUtils.substringBetween(mimeMessage.getHeader("From")[0], "<", ">"));
        message.put(FROM_NAME_KEY, StringUtils.substringBefore(mimeMessage.getHeader("From")[0], " <"));
		} else {
			message.put(FROM_KEY, mimeMessage.getHeader("From")[0]);
		}
        if (mimeMessage.getHeader("To") != null) {
        	if (mimeMessage.getHeader("To")[0].contains("<")) {
	        	message.put(TO_KEY, StringUtils.substringBetween(mimeMessage.getHeader("To")[0], "<", ">"));
	        	message.put(TO_NAME_KEY, StringUtils.substringBefore(mimeMessage.getHeader("To")[0], " <"));
        	} else {
        		message.put(TO_KEY, mimeMessage.getHeader("To")[0]);
        	}
        } else {
        	message.put(TO_KEY, profile.getEmailAddress());
        }
        if (mimeMessage.getHeader("Cc") != null)
        	message.put(CC_KEY, StringUtils.substringsBetween(mimeMessage.getHeader("Cc")[0], "<", ">"));
        message.put(SUBJECT_KEY, mimeMessage.getHeader("Subject")[0]);
		return message;
	}

	@Override
	public void setTriggerRefresh(TriggerRefresh triggerRefresh) {
		this.triggerRefresh = triggerRefresh;
	}
}