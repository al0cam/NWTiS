package org.foi.nwtis.bsikac.zrna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.bsikac.jpa.criteriaapi.KorisniciJpa;
import org.foi.nwtis.bsikac.jpa.entiteti.Korisnici;
import org.foi.nwtis.bsikac.transfer.KorisniciKlijent;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RequestScoped
@Named("prijava")
@NoArgsConstructor
public class Prijava {
	@EJB
	KorisniciJpa korisniciJpa;

	List<Korisnici> korisnici = new ArrayList<>();
	@Getter
	@Setter
	private String korisnik;
	@Getter
	@Setter
	private String lozinka;
	@Getter
	@Setter
	private String statusPrijave = null;
	
	public List<Korisnici> getKorisnici() {
		korisnici = korisniciJpa.findAll();
		return korisnici;
	}

	public void setKorisnici(List<Korisnici> korisnici) {
		this.korisnici = korisnici;
	}
	
	public void prijava()
	{
		boolean postojiUBazi = false;
		getKorisnici();
		for(Korisnici k : korisnici)
		{
			if(k.getKorime().equals(korisnik) && k.getLozinka().equals(lozinka))
			{
				postojiUBazi = true;
				break;
			}
		}
		if(postojiUBazi)
		{
			if(KorisniciKlijent.prijava(korisnik, lozinka))
				statusPrijave = "Prijava je uspjesna";
			else 
				statusPrijave = "Prijava je neuspjesna";
		}
		else 
			statusPrijave = "Korisnik ne postoji u bazi podataka";
	}
	
	
	public void odjava()
	{
		if(KorisniciKlijent.korisnickiZeton == null)
		{
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("pogreska.xhtml");
			} catch (IOException e) {
			}
			statusPrijave = "Prijava je neuspjesna";
		}
		else 
		{
			if(KorisniciKlijent.brisiZeton())
				statusPrijave = "odjava je uspjesna";
			else 
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect("pogreska.xhtml");
				} catch (IOException e) {
				}
		}
			
	}
}
