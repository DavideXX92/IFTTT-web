package it.ifttt.repository;

import java.util.Set;

import org.springframework.data.repository.Repository;

import it.ifttt.domain.Role;
import it.ifttt.domain.User;

public interface RoleRepository extends Repository<Role, Integer> {
	
	Set<Role> findAll();
	
	Set<Role> getByUser(User user);
	
	Role save(Role role);

}
