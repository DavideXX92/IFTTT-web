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
import javax.persistence.Transient;

@Entity
public class Ingredient {
	
	private int idIngr;
	private String nameIngr;
	private String type;
	private Integer idT;
	private Integer idChT;
	private Integer idA;
	private Integer idChA;
	private String tag;
	private boolean visualizableToClient;
	
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

	/*@Access(AccessType.PROPERTY)
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
	}*/

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isVisualizableToClient() {
		return visualizableToClient;
	}

	public void setVisualizableToClient(boolean visualizableToClient) {
		this.visualizableToClient = visualizableToClient;
	}

	@JoinColumn(name="idT")
	public Integer getIdT() {
		return idT;
	}

	public void setIdT(Integer idT) {
		this.idT = idT;
	}

	@JoinColumn(name="idChT")
	public Integer getIdChT() {
		return idChT;
	}

	public void setIdChT(Integer idChT) {
		this.idChT = idChT;
	}

	@JoinColumn(name="idCh")
	public Integer getIdA() {
		return idA;
	}

	@JoinColumn(name="idA")
	public void setIdA(Integer idA) {
		this.idA = idA;
	}

	public Integer getIdChA() {
		return idChA;
	}

	public void setIdChA(Integer idChA) {
		this.idChA = idChA;
	}



	

	
	
}
