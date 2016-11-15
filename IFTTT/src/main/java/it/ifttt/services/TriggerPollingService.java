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
	
	@Autowired
	TriggerHandler triggerHandler;
	
	@Scheduled(fixedDelay=5000)
	public void triggerCheckEvent(){
		List<Recipe> recipes = recipeRepository.findAll();
		try{
			for(Recipe recipe : recipes){
				System.out.println("Ricetta trovata: " + recipe.getIdR());
				List<User> users = userRepository.getUsersWithAtLeastArecipeActive(recipe.getIdR());
				Trigger trigger = recipe.getTrigger();
				System.out.println("Trigger attivato: " + trigger.getNameT());
				triggerHandler.initialize(trigger);
				
				for(User user : users){
					System.out.println("Utente: " + user.getUsername());
					List<UserIngredient> ingredients = userIngredientRepository.findAll(user.getId(), recipe.getIdR());
					List<Object> triggerParams = triggerHandler.raise(user, ingredients, recipe);
					if(triggerParams.size() > 0){
						System.out.println("Evento scatenato!");
						for(Object params : triggerParams){
							
						}
					}
				}
			}
		}catch(Exception e){
			
		}
	}
}
