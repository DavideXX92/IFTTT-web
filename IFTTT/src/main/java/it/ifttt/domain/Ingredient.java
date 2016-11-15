package it.ifttt.domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Ingredient {
	
	private int idIngr;
	private String nameIngr;
	private String type;
	private Trigger trigger;
	private Action action;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getIdIngr() {
		return idIngr;
	}
	
	public void setIdIngr(int idIngr) {
		this.idIngr = idIngr;
	}
	
	public String getNameIngr() {
		return nameIngr;
	}
	
	public void setNameIngr(String nameIngr) {
		this.nameIngr = nameIngr;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumns({
		@JoinColumn(name="idA", referencedColumnName="idA"),
		@JoinColumn(name="idChA", referencedColumnName="idCh")
	})
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumns({
		@JoinColumn(name="idT", referencedColumnName="idT"),
		@JoinColumn(name="idChT", referencedColumnName="idCh")
	})
	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}
	
	
}
