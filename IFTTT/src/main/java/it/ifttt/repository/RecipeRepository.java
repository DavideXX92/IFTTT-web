package it.ifttt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import it.ifttt.domain.Recipe;

public interface RecipeRepository extends Repository<Recipe, Integer> {
	
	@Query(value = "SELECT * FROM recipe WHERE control = ?1", nativeQuery = true)
	Recipe findOne(String control);
	Recipe save(Recipe recipe);
}
