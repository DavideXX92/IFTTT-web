package it.ifttt.repository;

import java.util.Set;

import org.springframework.data.repository.Repository;

import it.ifttt.model.user.Role;
import it.ifttt.model.user.User;

public interface RoleRepository extends Repository<Role, Integer> {
	
	Set<Role> findAll();
	
	Set<Role> getByUser(User user);
	
	Role save(Role role);

}
