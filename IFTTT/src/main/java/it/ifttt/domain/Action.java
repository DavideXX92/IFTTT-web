package it.ifttt.domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;

@Entity
public class Action {
	
	@EmbeddedId
    private ActionPK idA;
	private String nameA;
	private List<Ingredient> ingredients;
	//private Channel channel;
		
	public ActionPK getIdA() {
		return idA;
	}

	public void setIdA(ActionPK idA) {
		this.idA = idA;
	}

	public String getNameA() {
		return nameA;
	}
	
	public void setNameA(String nameA) {
		this.nameA = nameA;
	}
	
	@Access(AccessType.PROPERTY)
	@OneToMany(fetch=FetchType.EAGER, mappedBy="action")
	public List<Ingredient> getIngredients() {
		return ingredients;
	}
	
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	/*@Access(AccessType.PROPERTY)
	@MapsId("idCh")
	@JoinColumn(name="idCh", insertable=false, updatable=false)
	@ManyToOne(fetch=FetchType.LAZY)
	public Channel getChannel() {
		return channel;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}*/
	

}
