package it.ifttt.domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="myTrigger")
public class Trigger {
	
	@EmbeddedId
    private TriggerPK idT;
	private String nameT;
	//private Channel channel;
	private List<Ingredient> ingredients;

	public TriggerPK getIdT() {
		return idT;
	}
	
	public void setIdT(TriggerPK idT) {
		this.idT = idT;
	}
	
	public String getNameT() {
		return nameT;
	}
	
	public void setNameT(String nameT) {
		this.nameT = nameT;
	}
	
	@Access(AccessType.PROPERTY)
	//@OneToMany(fetch=FetchType.EAGER, mappedBy="trigger")
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumns({
		@JoinColumn(name="idT", referencedColumnName="idT"),
		@JoinColumn(name="idChT", referencedColumnName="idCh")
	})
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
