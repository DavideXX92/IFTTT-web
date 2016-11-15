package it.ifttt.domain;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.MapsId;

public class UserIngredientPK implements Serializable {
	
	//@MapsId("idU")
	@JoinColumn(name="idU")
	private int idU;
	
	//@MapsId("idR")
	@JoinColumn(name="idR")
	private int idR;
	
	//@MapsId("idIngr")
	@JoinColumn(name="idIngr")
	private int idIngr;

	public int getIdU() {
		return idU;
	}

	public void setIdU(int idU) {
		this.idU = idU;
	}

	public int getIdR() {
		return idR;
	}

	public void setIdR(int idR) {
		this.idR = idR;
	}

	public int getIdIngr() {
		return idIngr;
	}

	public void setIdIngr(int idIngr) {
		this.idIngr = idIngr;
	}
}
