package it.ifttt.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import it.ifttt.domain.Action;

public interface ActionRepository extends Repository<Action, Integer> {
	
	@Query(value = "SELECT * FROM action WHERE idCh = ?1", nativeQuery = true)
	Set<Action> findActionsByChannelId(int idChannel);
	Action save(Action action);
}
