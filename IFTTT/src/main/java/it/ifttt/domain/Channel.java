package it.ifttt.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Channel {
	
	private int idCh;
	private String nameCh;
	private List<Trigger> triggers;
	private List<Action> actions;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getIdCh() {
		return idCh;
	}
	
	public void setIdCh(int idCh) {
		this.idCh = idCh;
	}
	
	public String getNameCh() {
		return nameCh;
	}
	
	public void setNameCh(String nameCh) {
		this.nameCh = nameCh;
	}
	
	//@OneToMany(fetch=FetchType.EAGER, mappedBy="channel")
	@OneToMany(fetch=FetchType.EAGER)
	public List<Trigger> getTriggers() {
		return triggers;
	}
	
	public void setTriggers(List<Trigger> triggers) {
		this.triggers = triggers;
	}
	
	//@OneToMany(fetch=FetchType.EAGER, mappedBy="channel")
	@OneToMany(fetch=FetchType.EAGER)
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
}
