package it.ifttt.polling;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ifttt.action.ActionPerformer;
import it.ifttt.domain.Action;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.Recipe;
import it.ifttt.domain.Trigger;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;
import it.ifttt.gmail.action.SendEmail;
import it.ifttt.repository.IngredientRepository;

@Component
public class ActionHandler {
	
	@Autowired
	SendEmail sendEmail;
	@Autowired
	IngredientRepository ingredientRepository;
	
	private ActionPerformer actionPerformer;
	private Action action;
	
	public void initialize(Action action){
		this.action = action;
		this.actionPerformer = setAction(action.getNameA());
	}
	
	public void perform(User user, List<UserIngredient> ingredients, Object objIngredients){
		try{
			actionPerformer.setUser(user);
		}catch(Exception e){
			System.out.println("Errore: " + e.getMessage());
			return;
		}
		actionPerformer.setActionIngredients(ingredients);
		List<Ingredient> injectableIngredients = ingredientRepository.getIngredientsByAction(action.getIdA().idA, action.getIdA().idCh);
		//actionPerformer.injectIngredients(injectableIngredients, objIngredients);
		actionPerformer.perform();
	}
	
	private ActionPerformer setAction(String action){
		ActionPerformer actionPerformer = null;
		
		switch(action){
			case "SEND_EMAIL":
				actionPerformer = sendEmail;
				break;
			case "CREATE_CALENDAR_EVENT":
				break;
		}
		
		return actionPerformer;
	}
}
