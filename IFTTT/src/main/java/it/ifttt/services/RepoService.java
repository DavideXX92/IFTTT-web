package it.ifttt.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.ifttt.domain.Action;
import it.ifttt.domain.Channel;
import it.ifttt.domain.Recipe;
import it.ifttt.domain.User;
import it.ifttt.domain.UserIngredient;
import it.ifttt.repository.ActionRepository;
import it.ifttt.repository.ChannelRepository;
import it.ifttt.repository.RecipeRepository;
import it.ifttt.repository.TriggerRepository;
import it.ifttt.repository.UserIngredientRepository;
import it.ifttt.repository.UserRepository;

@Service
@Transactional
public class RepoService {
	
	@Autowired
	private UserRepository UserRepo;
	@Autowired
	private ChannelRepository chRepo;
	@Autowired
	private TriggerRepository triggerRepo;
	@Autowired
	private ActionRepository actionRepo;
	@Autowired
	private RecipeRepository recipeRepo;
	@Autowired
	private UserIngredientRepository userIngrRepo;
	
	public Set<Channel> getChannels(){
		return chRepo.findAll();
	}
	public Channel getChannel(int idChannel){
		return chRepo.findOne(idChannel);
	}
	public Set<Action> getActions(int idChannel){
		return actionRepo.findActionsByChannelId(idChannel);
	}
	public Recipe saveRecipe(Recipe recipe){
		Recipe recipeDB = recipeRepo.findOne(recipe.getControl());
		if(recipeDB==null){
			System.out.println("La ricetta e' nuova, la salvo nel db");
			return recipeRepo.save(recipe);
		}
		else{
			System.out.println("La ricetta e' gia' presente nel db, ritorno quella gia' esistente");
			return recipeDB;
		}	
	}
	
	public List<User> getUsersWithAtLeastArecipeActive(int idR){
		return UserRepo.getUsersWithAtLeastArecipeActive(idR);
	}
	
	public List<Recipe> getRecipes(){
		return recipeRepo.findAll();
	}
	
	public Recipe getRecipe(String control){
		return recipeRepo.findOne(control);
	}
	
	public List<UserIngredient> getIngredients(int idU, int idR){
		return userIngrRepo.findAll(idU, idR);
	}
}
