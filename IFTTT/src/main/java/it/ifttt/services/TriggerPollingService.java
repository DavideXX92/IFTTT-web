package it.ifttt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

import it.ifttt.domain.Ingredient;
import it.ifttt.domain.Recipe;
import it.ifttt.domain.Trigger;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;
import it.ifttt.polling.TriggerHandler;
import it.ifttt.repository.RecipeRepository;
import it.ifttt.repository.UserIngredientRepository;
import it.ifttt.repository.UserRepository;

@Service
public class TriggerPollingService {
	@Autowired
	RecipeRepository recipeRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserIngredientRepository userIngredientRepository;
	
	@Scheduled(fixedDelay=5000)
	public void triggerCheckEvent(){
		List<Recipe> recipes = recipeRepository.getRecipeByUser>0();
		
		for(Recipe recipe : recipes){
			List<User> users = userRepository.getUserByRecipeActive(recipe.getIdR());
			Trigger trigger = recipe.getTrigger();
			TriggerHandler triggerHandler = new TriggerHandler(trigger);
			
			for(User user : users){
				List<UserIngredient> ingredients = userIngredientRepository.getIngredientsFromUserIdAndRecipeId();
				List<Object> triggerParams = triggerHandler.raise(user, ingredients);
				if(triggerParams.size() > 0){
					System.out.println("Evento scatenato!");
					for(Object params : triggerParams){
						
					}
				}
			}
		}
	}
}
