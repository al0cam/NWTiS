package org.foi.nwtis.bsikac.aplikacija_4.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.bsikac.aplikacija_4.modeli.Zeton;
import org.foi.nwtis.bsikac.aplikacija_4.mvc.MVC;
import org.foi.nwtis.bsikac.aplikacija_4.slusaci.SlusacAplikacije;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.podaci.AvionLeti;

import com.google.gson.Gson;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
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
			MVC.korisnickiZeton = zeton;
			MVC.korisnikUGrupiZaBrisanje = uGrupiAdmin(korisnik);
			MVC.korisnik = new Korisnik();
			MVC.korisnik.setKorIme(korisnik);
			MVC.korisnik.setLozinka(lozinka);
			return true;
		}
		return false;
	}
	
	
	public static boolean uGrupiAdmin(String korisnik) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/korisnici/").path(korisnik).path("/grupe/admin");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", MVC.korisnickiZeton.getKorisnik()).header("zeton",MVC.korisnickiZeton.getZeton())
				.get();
		if (restOdgovor.getStatus() == 200) {
			return true;
		}
		return false;
	}
	
	public static boolean registracija(Korisnik korisnik)
	{
		postaviDbKonf();
		MVC.korisnik = korisnik;
		if(MVC.adminZeton == null)
			MVC.adminZeton = dohvatiNoviZeton(admin, lozinka);
	
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/korisnici");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", admin).header("zeton",MVC.adminZeton.getZeton())
				.post(Entity.json(korisnik));
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			System.out.println("ODGOVOR:" + odgovor);
			return true;
		}
		return false;
	}	
	
	public static List<Korisnik> dohvatiKorisnike(Korisnik korisnik)
	{
		postaviDbKonf();
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/korisnici");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", MVC.korisnickiZeton.getKorisnik()).header("zeton",MVC.korisnickiZeton.getZeton())
				.get();
		
		List<Korisnik> korisnici = null;
		
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			korisnici = new ArrayList<>();
			korisnici.addAll(Arrays.asList(gson.fromJson(odgovor, Korisnik[].class)));
		}
		return korisnici;
	}	
	
	public static boolean brisiZeton()
	{
		postaviDbKonf();
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/provjere/"+MVC.korisnickiZeton.getZeton());
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", MVC.korisnickiZeton.getKorisnik()).header("zeton",MVC.korisnickiZeton.getZeton())
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
				.header("Accept", "application/json").header("korisnik", MVC.korisnik.getKorIme()).header("zeton",MVC.korisnik.getLozinka())
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
