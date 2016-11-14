package it.ifttt.domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="myTriggerRefresh")
public class TriggerRefresh {

	@EmbeddedId
	private TriggerRefreshPK idTr;
	private Date lastRefresh;
	private User user;
	private Recipe recipe;
	private Trigger trigger;
	
	public TriggerRefreshPK getIdTr() {
		return idTr;
	}
	
	public void setIdTr(TriggerRefreshPK idTr) {
		this.idTr = idTr;
	}
	
	public Date getLastRefresh() {
		return lastRefresh;
	}
	
	public void setLastRefresh(Date lastRefresh) {
		this.lastRefresh = lastRefresh;
	}
	
	@Access(AccessType.PROPERTY)
	@MapsId("idU")
	@JoinColumn(name="idU")
	@ManyToOne
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@Access(AccessType.PROPERTY)
	@MapsId("idR")
	@JoinColumn(name="idR")
	@ManyToOne
	public Recipe getRecipe() {
		return recipe;
	}
	
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	
	@Access(AccessType.PROPERTY)
	@MapsId("idT")
	@JoinColumns({
		@JoinColumn(name="idT", referencedColumnName="idT"),
		@JoinColumn(name="idChT", referencedColumnName="idCh")
	})
	@ManyToOne
	public Trigger getTrigger() {
		return trigger;
	}
	
	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}
}
