package org.foi.nwtis.bsikac.aplikacija_4.mvc;

import java.util.List;

import org.foi.nwtis.bsikac.aplikacija_4.modeli.Zeton;
import org.foi.nwtis.bsikac.aplikacija_4.transfer.KorisniciKlijent;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.podaci.AvionLeti;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

/**
 * Klasa PregledAerodroma.
 */
@Controller
@Path("/korisnici")
@RequestScoped
public class PregledKorisnika {

	/** model. */
	@Inject
	private Models model;

	@GET
	@Path("/pocetak")
	@View("index.jsp")
	public void pocetak() {
	}
	
	@GET
	@Path("/registracija")
	@View("registracija.jsp")
	public void registracija() {
	}

	@POST
	@Path("/registracija")
	@View("registracija.jsp")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void registracija(@FormParam("korIme") String korIme, @FormParam("ime") String ime,
			@FormParam("prezime") String prezime, @FormParam("email") String email,
			@FormParam("lozinka") String lozinka) {
		
		Korisnik k = new Korisnik();
		k.setEmail(email);
		k.setIme(ime);
		k.setPrezime(prezime);
		k.setLozinka(lozinka);
		k.setKorIme(korIme);
		boolean reg = KorisniciKlijent.registracija(k);
		if(reg)
			model.put("reg", "uspjesna registracija");
		else
			model.put("reg", "neuspjesna registracija");
	}

	
	@GET
	@Path("/prijava")
	@View("prijava.jsp")
	public void prijava() {
	}
	
	@POST
	@Path("/prijava")
	@View("prijava.jsp")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void prijava(@FormParam("korisnik") String korIme, @FormParam("lozinka") String lozinka) {
		boolean uspjeh = KorisniciKlijent.prijava(korIme, lozinka);
		if(uspjeh)
			model.put("status", "uspjesna prijava");
		else 
			model.put("status", "neuspjesna prijava");
	}
	
	@GET
	@Path("/pregledKorisnika")
	@View("pregledKorisnika.jsp")
	public void pregledKorisnika() {
		if(MVC.korisnickiZeton == null)
			model.put("prijavljen", "potrebna je prijava za pregled podataka");
		else
		{
			List<Korisnik> k = KorisniciKlijent.dohvatiKorisnike(MVC.korisnik);
			model.put("korisnici", k);
			model.put("korisnikUGrupiZaBrisanje", MVC.korisnikUGrupiZaBrisanje);
		}
		
	}

	@POST
	@Path("/pregledKorisnika")
	@View("pregledKorisnika.jsp")
	public void brisiZeton() {
		boolean obrisano = KorisniciKlijent.brisiZeton();
		if(obrisano)
			model.put("zeton", "brisanje uspjesno provedeno");
		else
			model.put("zeton", "brisanje neuspjesno");
	}
	
	@POST
	@Path("/pregledKorisnika/{korisnik}")
	@View("pregledKorisnika.jsp")
	public void brisiZetoneKorisnika(@PathParam("korisnik") String korisnik) {
		boolean obrisano = KorisniciKlijent.brisiZetoneZaKorisnika(korisnik);
		if(obrisano)
			model.put("zeton", "brisanje uspjesno provedeno");
		else
			model.put("zeton", "brisanje neuspjesno");

		List<Korisnik> k = KorisniciKlijent.dohvatiKorisnike(MVC.korisnik);
		model.put("korisnici", k);
		model.put("korisnikUGrupiZaBrisanje", MVC.korisnikUGrupiZaBrisanje);
	}
	
}
