package it.ifttt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import it.ifttt.domain.Ingredient;

public interface IngredientRepository extends Repository<Ingredient, Integer>{
	
	@Query(value = "SELECT * FROM ingredient WHERE idChT = ?1 AND idT = ?2", nativeQuery = true)
	List<Ingredient> findAllByTrigger(int idChT, int idT);
	
	@Query(value = "SELECT * FROM ingredient WHERE idChA = ?1 AND idA = ?2", nativeQuery = true)
	List<Ingredient> findAllByAction(int idChA, int idA);
	
	@Query(value = "SELECT * FROM ingredient WHERE idIngr = ?1", nativeQuery = true)
	Ingredient findOne(int idIngr);
	
}
