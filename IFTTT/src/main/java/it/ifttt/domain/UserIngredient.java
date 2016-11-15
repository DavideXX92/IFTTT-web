package it.ifttt.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class UserIngredient {

	@EmbeddedId
    private UserIngredientPK idUI;
	private String value;
	
	public UserIngredientPK getIdUI() {
		return idUI;
	}
	public void setIdUI(UserIngredientPK idUI) {
		this.idUI = idUI;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
