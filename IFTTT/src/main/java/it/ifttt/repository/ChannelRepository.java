package it.ifttt.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import it.ifttt.domain.Channel;

public interface ChannelRepository extends Repository<Channel, Integer> {
	
	Set<Channel> findAll();	
	Channel findOne(Integer idChannel);
	Channel save(Channel channel);
}
