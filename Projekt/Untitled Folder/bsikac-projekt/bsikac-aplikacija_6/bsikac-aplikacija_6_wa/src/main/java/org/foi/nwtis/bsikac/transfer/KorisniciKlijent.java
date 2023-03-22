package org.foi.nwtis.bsikac.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.bsikac.jpa.entiteti.Korisnici;
import org.foi.nwtis.bsikac.modeli.Zeton;
import org.foi.nwtis.bsikac.slusaci.SlusacAplikacije;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

import com.google.gson.Gson;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

/**
 * Klasa AerodromiKlijent.
 */
public class KorisniciKlijent {
	
	private static PostavkeBazaPodataka dbKonf = null;
	private static String admin = null;
	private static String lozinka = null;
	public static Zeton adminZeton = null;
	public static Zeton korisnickiZeton = null;
	public static Korisnici ulogiraniKorisnik = null;
	public static boolean korisnikUGrupiZaBrisanje = false;
	
	
	public static Zeton dohvatiNoviZeton(String korisnik, String lozinka) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/provjere");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", korisnik).header("lozinka",lozinka)
				.get();
		Zeton zeton = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			System.out.println("ODGOVOR:" + odgovor);
			Gson gson = new Gson();
			zeton = gson.fromJson(odgovor, Zeton.class);
			zeton.setKorisnik(korisnik);
			zeton.setLozinka(lozinka);
			zeton.setStatus(1);
		}
		return zeton;
	}
	
	public static boolean prijava(String korisnik, String lozinka)
	{
		Zeton zeton = dohvatiNoviZeton(korisnik, lozinka);
		if(zeton != null)
		{
			korisnickiZeton = zeton;
			korisnikUGrupiZaBrisanje = uGrupiAdmin(korisnik);
			ulogiraniKorisnik = new Korisnici();
			ulogiraniKorisnik.setKorime(korisnik);
			ulogiraniKorisnik.setLozinka(lozinka);
			return true;
		}
		return false;
	}
	
	
	public static boolean uGrupiAdmin(String korisnik) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/korisnici/").path(korisnik).path("/grupe/admin");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", korisnickiZeton.getKorisnik()).header("zeton",korisnickiZeton.getZeton())
				.get();
		if (restOdgovor.getStatus() == 200) {
			return true;
		}
		return false;
	}
	
	public static boolean registracija(Korisnici korisnik)
	{
		postaviDbKonf();
		ulogiraniKorisnik = korisnik;
		if(adminZeton == null)
			adminZeton = dohvatiNoviZeton(admin, lozinka);
	
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/korisnici");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", admin).header("zeton",adminZeton.getZeton())
				.post(Entity.json(korisnik));
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			System.out.println("ODGOVOR:" + odgovor);
			return true;
		}
		return false;
	}	
	
	public static List<Korisnici> dohvatiKorisnike()
	{
		postaviDbKonf();
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/korisnici");
		Response restOdgovor;
		if(korisnickiZeton == null)
		{
			
			if(adminZeton == null)
				adminZeton = dohvatiNoviZeton(admin, lozinka);
			restOdgovor = webResource.request()
					.header("Accept", "application/json").header("korisnik", adminZeton.getKorisnik()).header("zeton",adminZeton.getZeton())
					.get();
		}
		else
			restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", korisnickiZeton.getKorisnik()).header("zeton", korisnickiZeton.getZeton())
				.get();
		
		List<Korisnici> korisnici = null;
		
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			korisnici = new ArrayList<>();
			korisnici.addAll(Arrays.asList(gson.fromJson(odgovor, Korisnici[].class)));
		}
		return korisnici;
	}	
	
	public static boolean brisiZeton()
	{
		postaviDbKonf();
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/provjere/"+korisnickiZeton.getZeton());
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", korisnickiZeton.getKorisnik()).header("zeton",korisnickiZeton.getZeton())
				.delete();
		
		if (restOdgovor.getStatus() == 200) {
			return true;
		}
		return false;
	}		
	
	public static boolean brisiZetoneZaKorisnika(String korisnik)
	{
		postaviDbKonf();
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/provjere/korisnik").path(korisnik);
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", ulogiraniKorisnik.getKorime()).header("zeton",ulogiraniKorisnik.getLozinka())
				.delete();
		
		if (restOdgovor.getStatus() == 200) {
			return true;
		}
		return false;
	}	
	
	
	
	public static void postaviDbKonf()
	{
		if(dbKonf == null)
		{
			dbKonf = SlusacAplikacije.getDbKonf();
			admin = dbKonf.dajPostavku("sustav.korisnik");
			lozinka = dbKonf.dajPostavku("sustav.lozinka");
		}
	}
}
