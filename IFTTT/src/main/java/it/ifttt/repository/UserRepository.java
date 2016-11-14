package it.ifttt.repository;


import org.springframework.data.repository.Repository;

import it.ifttt.domain.User;

public interface UserRepository extends Repository<User, Integer> {

	User getById(int id);
	User findByUsername(String username);
	User save(User user);
	  
}
