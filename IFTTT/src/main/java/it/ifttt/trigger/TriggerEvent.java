package it.ifttt.trigger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import it.ifttt.domain.TriggerRefresh;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;

public interface TriggerEvent {	
	List<Object> raise(User user, List<UserIngredient> ingredients) throws IOException;
	void setUser(User user) throws IOException, GeneralSecurityException;
	void setIngredients(List<UserIngredient> userIngredients);
	void setTriggerRefresh(TriggerRefresh triggerRefresh);
}
