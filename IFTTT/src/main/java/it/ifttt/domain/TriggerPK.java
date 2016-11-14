package it.ifttt.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class TriggerPK implements Serializable {
	public int idT;
	public int idCh;
}