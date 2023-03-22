package org.foi.nwtis.bsikac.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.bsikac.jpa.criteriaapi.GrupeJpa;
import org.foi.nwtis.bsikac.jpa.criteriaapi.KorisniciJpa;
import org.foi.nwtis.bsikac.jpa.entiteti.Grupe;
import org.foi.nwtis.bsikac.jpa.entiteti.Korisnici;
import org.foi.nwtis.bsikac.transfer.KorisniciKlijent;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

//@ConversationScoped
@RequestScoped
@Named("pregledKorisnika")
public class KorisniciZrnoCriteriaApi implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	KorisniciJpa korisniciJpa;

	@EJB
	GrupeJpa grupeJpa;

	List<Korisnici> korisnici = null;
	List<Korisnici> dohvaceniKorisnici = new ArrayList<>();
	Korisnici korisnik = new Korisnici();
	Grupe grupa = new Grupe();

	boolean traziGrupe = false;

	public List<Korisnici> getKorisnici() {
		korisnici = korisniciJpa.findAll();
		return korisnici;
	}

	public void setKorisnici(List<Korisnici> korisnici) {
		this.korisnici = korisnici;
	}

	public List<Korisnici> getDohvaceniKorisnici() {
		dohvaceniKorisnici = KorisniciKlijent.dohvatiKorisnike();
		return dohvaceniKorisnici;
	}

	public void setDohvaceniKorisnici(List<Korisnici> korisnici) {
		this.dohvaceniKorisnici = korisnici;
	}
	
	public Korisnici getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnici korisnik) {
		this.korisnik = korisnik;
	}

	public Grupe getGrupa() {
		return grupa;
	}

	public void setGrupa(Grupe grupa) {
		this.grupa = grupa;
	}

	public String odabirKorisnika(String korisnikId) {
		this.korisnik = korisniciJpa.find(korisnikId);
		return "";
	}

	public String odabirGrupe(String grupaId) {
		this.grupa = grupeJpa.find(grupaId);
		traziGrupe = true;
		this.korisnici = this.grupa.getKorisnicis();
		return "";
	}
	
	public void sinkroniziraj()
	{
		for(Korisnici k: dohvaceniKorisnici)
		{
			if(!uLokalnojBazi(k.getKorime()))
			{
				korisniciJpa.create(k);
			}
		}
	}
	
	public boolean uLokalnojBazi(String korisnik) {
		if(korisnici == null)
			getKorisnici();
		for(Korisnici k : korisnici)
			if(k.getKorime().equals(korisnik))
				return true;
		return false;
		
	}
	

}