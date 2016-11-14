package it.ifttt.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import it.ifttt.domain.Recipe;
import it.ifttt.domain.Trigger;
import it.ifttt.domain.TriggerRefresh;
import it.ifttt.domain.User;

public interface TriggerRefreshRepository extends Repository<TriggerRefresh, Integer> {
	
	TriggerRefresh save(TriggerRefresh triggeRefresh);
	
	@Query(value = "SELECT * FROM triggerRefresh WHERE idU = ?1 AND idR = ?2 AND idT = ?3 AND idChT = ?4", nativeQuery = true)
	TriggerRefresh findOne(int idU, int idR, int idT, int idChT);
}
