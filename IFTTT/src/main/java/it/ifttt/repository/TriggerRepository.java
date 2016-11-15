package it.ifttt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import it.ifttt.domain.Trigger;

public interface TriggerRepository extends Repository<Trigger, Integer> {

	@Query(value = "SELECT * FROM trigger WHERE idCh = ?1", nativeQuery = true)
	List<Trigger> findTriggersByChannelId(int idChannel);

}
