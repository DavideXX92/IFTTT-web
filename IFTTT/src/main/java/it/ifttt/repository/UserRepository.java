package it.ifttt.repository;

import org.springframework.data.repository.Repository;

import it.ifttt.model.user.User;

public interface UserRepository extends Repository<User, Long> {

	User getById(long id);
	
	User findByUsername(String username);
	
	User save(User user);
}
