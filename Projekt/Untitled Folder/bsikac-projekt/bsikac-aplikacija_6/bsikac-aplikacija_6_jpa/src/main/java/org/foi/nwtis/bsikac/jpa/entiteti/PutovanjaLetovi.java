package org.foi.nwtis.bsikac.jpa.entiteti;

import java.io.Serializable;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
/**
 * The persistent class for the PUTOVANJA_LETOVI database table.
 * 
 */
@Entity
@Table(name="PUTOVANJA_LETOVI")
@NamedQuery(name="PutovanjaLetovi.findAll", query="SELECT p FROM PutovanjaLetovi p")
public class PutovanjaLetovi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String avion;

	private int vrijemeleta;

	//bi-directional many-to-one association to Putovanja
	@ManyToOne
	@JoinColumn(name="PUTOVANJE")
	private Putovanja putovanja;

	public PutovanjaLetovi() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAvion() {
		return this.avion;
	}

	public void setAvion(String avion) {
		this.avion = avion;
	}

	public int getVrijemeleta() {
		return this.vrijemeleta;
	}

	public void setVrijemeleta(int vrijemeleta) {
		this.vrijemeleta = vrijemeleta;
	}

	public Putovanja getPutovanja() {
		return this.putovanja;
	}

	public void setPutovanja(Putovanja putovanja) {
		this.putovanja = putovanja;
	}

}