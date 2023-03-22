package org.foi.nwtis.bsikac.jpa.entiteti;

import java.io.Serializable;
import java.util.List;


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
 * The persistent class for the PUTOVANJA database table.
 * 
 */
@Entity
@NamedQuery(name="Putovanja.findAll", query="SELECT p FROM Putovanja p")
public class Putovanja implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String aerodrompocetni;

	private String aerodromzavrsni;

	private int vrijemeprvogleta;

	//bi-directional many-to-one association to Korisnici
	@ManyToOne
	@JoinColumn(name="KORIME")
	private Korisnici korisnici;

	//bi-directional many-to-one association to PutovanjaLetovi
	@OneToMany(mappedBy="putovanja")
	private List<PutovanjaLetovi> putovanjaLetovis;

	public Putovanja() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAerodrompocetni() {
		return this.aerodrompocetni;
	}

	public void setAerodrompocetni(String aerodrompocetni) {
		this.aerodrompocetni = aerodrompocetni;
	}

	public String getAerodromzavrsni() {
		return this.aerodromzavrsni;
	}

	public void setAerodromzavrsni(String aerodromzavrsni) {
		this.aerodromzavrsni = aerodromzavrsni;
	}

	public int getVrijemeprvogleta() {
		return this.vrijemeprvogleta;
	}

	public void setVrijemeprvogleta(int vrijemeprvogleta) {
		this.vrijemeprvogleta = vrijemeprvogleta;
	}

	public Korisnici getKorisnici() {
		return this.korisnici;
	}

	public void setKorisnici(Korisnici korisnici) {
		this.korisnici = korisnici;
	}

	public List<PutovanjaLetovi> getPutovanjaLetovis() {
		return this.putovanjaLetovis;
	}

	public void setPutovanjaLetovis(List<PutovanjaLetovi> putovanjaLetovis) {
		this.putovanjaLetovis = putovanjaLetovis;
	}

	public PutovanjaLetovi addPutovanjaLetovi(PutovanjaLetovi putovanjaLetovi) {
		getPutovanjaLetovis().add(putovanjaLetovi);
		putovanjaLetovi.setPutovanja(this);

		return putovanjaLetovi;
	}

	public PutovanjaLetovi removePutovanjaLetovi(PutovanjaLetovi putovanjaLetovi) {
		getPutovanjaLetovis().remove(putovanjaLetovi);
		putovanjaLetovi.setPutovanja(null);

		return putovanjaLetovi;
	}

}