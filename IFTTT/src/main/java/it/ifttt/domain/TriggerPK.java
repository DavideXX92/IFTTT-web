package it.ifttt.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;

@Embeddable
public class TriggerPK implements Serializable {
	
	public int idT;
	
	@MapsId("idCh")
	@JoinColumn(name="idCh", insertable=false, updatable=false)
	public int idCh;
}