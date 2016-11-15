package it.ifttt.domain;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.MapsId;

public class UserIngredientPK implements Serializable {
	
	//@MapsId("idU")
	@JoinColumn(name="idU")
	public int idU;
	
	//@MapsId("idR")
	@JoinColumn(name="idR")
	public int idR;
	
	//@MapsId("idIngr")
	@JoinColumn(name="idIngr")
	public int idIngr;
}
