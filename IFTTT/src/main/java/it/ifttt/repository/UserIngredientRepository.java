package it.ifttt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import it.ifttt.domain.UserIngredient;

public interface UserIngredientRepository extends Repository<UserIngredient, Integer>{

	@Query(value = "SELECT * FROM userIngredient WHERE idU = ?1 AND idR = ?2", nativeQuery = true)
	List<UserIngredient> findAll(int idU, int idR);
	
	UserIngredient save(UserIngredient userIngredient);
}
