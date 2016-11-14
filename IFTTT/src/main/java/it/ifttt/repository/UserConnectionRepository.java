package it.ifttt.repository;

import java.util.Set;

import org.springframework.data.repository.Repository;

import it.ifttt.domain.UserConnection;

public interface UserConnectionRepository extends Repository<UserConnection, String>{

	UserConnection findOneByUserIdAndProviderId(String username, String providerId);
	
	UserConnection findOneByUserId(String userId);
	
	Set<UserConnection> getByUserId(String userId);
	
	UserConnection save(UserConnection userConnection);
	
	void delete(UserConnection userConnection);
}
