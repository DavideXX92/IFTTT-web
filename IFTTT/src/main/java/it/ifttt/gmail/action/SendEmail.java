package it.ifttt.gmail.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import it.ifttt.action.ActionPerformer;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;
import it.ifttt.domain.UserIngredientPK;
import it.ifttt.repository.IngredientRepository;
import it.ifttt.repository.UserIngredientRepository;
import it.ifttt.social.GmailCreator;

@Component
public class SendEmail implements ActionPerformer{
	public static final String FROM_KEY = "from";
	public static final String TO_KEY = "to";
	public static final String CC_KEY = "cc";
	public static final String SUBJECT_KEY = "subject";
	public static final String LABELS_KEY = "labels";
	public static final String BODY_KEY = "body";
	
	public static final String FROM_NAME_KEY = "from-name";
	public static final String TO_NAME_KEY = "to-name";
	
	@Autowired
	private GmailCreator gmailCreator;
	
	@Autowired
	private IngredientRepository ingredientRepository;
	
	private User user;
	private List<UserIngredient> actionIngredient;
	private List<UserIngredient> injectedIngredients;
	private Gmail gmail;	
	
	@Override
	public void perform() {
		System.out.println("Performing action 'send-email' of channel Gmail...");
		
		// get main ingredients
		String to = resolveIngredient(TO_KEY);
		String subject = resolveIngredient(SUBJECT_KEY);
		String body = resolveIngredient(BODY_KEY);
		String threadId = null;
		String replyTo = null;
		/*if (getIngredient(THREAD_ID_KEY) != null)
			threadId = getIngredient(THREAD_ID_KEY).getValue();
		if (getIngredient(REPLY_TO_KEY) != null)
			replyTo = getIngredient(REPLY_TO_KEY).getValue();*/	
		
		try {			
			// create email
			String from = gmail.users().getProfile("me").execute().getEmailAddress();
			MimeMessage mime = createEmail(to, from, subject, body);
			if (replyTo != null)
				mime.addHeader("Reply-To", replyTo);
			Message message = createMessageWithEmail(mime);
			if (threadId != null)
				message.setThreadId(threadId);

			// send email
	        message = gmail.users().messages().send(from, message).execute();

	        System.out.println("Message id: " + message.getId());
	        System.out.println(message.toPrettyString());
	        
	        System.out.println("Performed action 'send-email' of channel Gmail.");			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void setUser(User user) throws IOException, GeneralSecurityException {
		this.user = user;
		this.gmail = gmailCreator.getGmail(user.getUsername());	
	}

	@Override
	public void setActionIngredients(List<UserIngredient> userIngredients) {
		this.actionIngredient = userIngredients;
	}
	
	private String resolveIngredient(String name){
		UserIngredient tmp = null;
		for(UserIngredient ui : actionIngredient){
			int ingredientId = ui.getIdUI().getIdIngr();
			if(ingredientRepository.findOne(ingredientId).getNameIngr().equals(name)){
				tmp = ui;
				break;
			}							
		}
		
		if(tmp.getValue() != null)
			return tmp.getValue(); //SOSTITUIRE CON FUNZIONE CHE RICERCA TAG
		
		tmp = null;
		for(UserIngredient ui : injectedIngredients){
			int ingredientId = ui.getIdUI().getIdIngr();
			if(ingredientRepository.findOne(ingredientId).getNameIngr().equals(name)){
				tmp = ui;
			}							
		}
		
		return tmp.getValue();
	}
	
	/**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to email address of the receiver
     * @param from email address of the sender, the mailbox account
     * @param subject subject of the email
     * @param bodyText body text of the email
     * @return the MimeMessage to be used to send email
     * @throws MessagingException
     */
    public static MimeMessage createEmail(String to,
                                          String from,
                                          String subject,
                                          String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage mime = new MimeMessage(session);

        mime.setFrom(new InternetAddress(from));
        mime.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        mime.setSubject(subject);
        mime.setText(bodyText);
        return mime;
    }
    
    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    public static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
    
    @Override
	public void injectIngredients(List<UserIngredient> injectedIngredients) {
		this.injectedIngredients = injectedIngredients;
	}
}
