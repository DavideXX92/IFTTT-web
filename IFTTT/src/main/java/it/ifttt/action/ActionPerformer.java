package it.ifttt.action;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;

public interface ActionPerformer {
	void perform();
	void setUser(User user) throws IOException, GeneralSecurityException;
	void setActionIngredients(List<UserIngredient> userIngredients);
}
