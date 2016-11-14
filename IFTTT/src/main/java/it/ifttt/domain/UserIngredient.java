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
	private User user;
	private Recipe recipe;
	private Ingredient ingredient;
	private String value;
	
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
	@MapsId("idIngr")
	@JoinColumn(name="idIngr")
	@ManyToOne
	public Ingredient getIngredient() {
		return ingredient;
	}
	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
