package it.ifttt.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ActionPK implements Serializable {
	public int idA;
	public int idCh;
}
