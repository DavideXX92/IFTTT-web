package it.ifttt.domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
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

import com.google.api.services.calendar.Calendar.Channels;

@Entity
public class Recipe {

	private Integer idR;
	private String control;
	private int nUsers;
	private boolean published;
	private Integer whoPublished;
	private Trigger trigger;
	private List<Action> actions;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idR")
	public Integer getIdR() {
		return idR;
	}
	public void setIdR(Integer idR) {
		this.idR = idR;
	}
	public String getControl() {
		return control;
	}
	public void setControl(String control) {
		this.control = control;
	}
	public int getnUsers() {
		return nUsers;
	}
	public void setnUsers(int nUsers){
		this.nUsers = nUsers;
	}
	public void incnUsers() {
		this.nUsers++;
	}
	public void decnUsers() {
		this.nUsers--;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	
	public Integer getWhoPublished() {
		return whoPublished;
	}
	public void setWhoPublished(Integer whoPublished) {
		this.whoPublished = whoPublished;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="actionRecipe", 
	 		   joinColumns={@JoinColumn(name="idR")},
	 		   inverseJoinColumns={@JoinColumn(name="idA"), @JoinColumn(name="idCh")})
	public List<Action> getActions() {
		return actions;
	}
	
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumns({
		@JoinColumn(name="idT",  referencedColumnName="idT", nullable=false),
		@JoinColumn(name="idCh", referencedColumnName="idCh", nullable=false)
	})
	public Trigger getTrigger() {
		return trigger;
	}
	
	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}
	
	public void generateControl(){
	this.control = "chT" + trigger.getIdT().idCh + "idT"+ trigger.getIdT().idT;
		for(int i=0; i<actions.size(); i++)
			this.control += "chA" + actions.get(i).getIdA().idCh + "idA" + actions.get(i).getIdA().idA;
	}
}
