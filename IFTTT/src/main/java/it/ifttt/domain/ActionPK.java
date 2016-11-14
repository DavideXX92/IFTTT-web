package it.ifttt.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;

@Embeddable
public class ActionPK implements Serializable {
	
	public int idA;
	
	@MapsId("idCh")
	@JoinColumn(name="idCh", insertable=false, updatable=false)
	public int idCh;
}
