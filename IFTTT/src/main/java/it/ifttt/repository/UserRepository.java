package it.ifttt.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import it.ifttt.domain.User;

public interface UserRepository extends Repository<User, Integer> {

	User getById(int id);
	
	User findByUsername(String username);
	
	User save(User user);
	
	@Query(value = "SELECT * FROM userRecipe, userifttt WHERE id=idU AND isActive=1 AND idR=?1", nativeQuery = true)
	List<User> getUsersWithAtLeastArecipeActive(int idR);
}
