package org.foi.nwtis.bsikac.aplikacija_5.transfer;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.foi.nwtis.bsikac.aplikacija_5.modeli.AerodromiPraceni;
import org.foi.nwtis.bsikac.aplikacija_5.slusaci.SlusacAplikacije;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa AerodromiKlijent.
 */
public class AerodromiKlijent {
	
	public static List<AvionLeti> dajPolaske(String korisnik, String zeton, String icao, String danOd, String danDo)
	{
		WebTarget webResource = null;
		Client client = ClientBuilder.newClient();
		long danOdLong = pretvoriDatumUSekunde(danOd);
		long danDoLong = pretvoriDatumUSekunde(danDo);
		if(danOdLong > 1 && danDoLong > 1)
			webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/aerodromi/"+icao+"/polasci?vrsta=1&vrijemeOd="+danOdLong+"&vrijemeDo="+danDoLong);
		else 
			webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/aerodromi/"+icao+"/polasci?vrsta=1&vrijemeOd="+danOd+"&vrijemeDo="+danDo);
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", korisnik).header("zeton",zeton)
				.get();
		
		List<AvionLeti> aerodrom = null;
		
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodrom = new ArrayList<>();
			aerodrom.addAll(Arrays.asList(gson.fromJson(odgovor, AvionLeti[].class)));
		}
		return aerodrom;
	}
	
	public static boolean dodajAerodromZaPracenje(String korisnik, String zeton, String icao)
	{
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/aerodromi");
		AerodromiPraceni ap = new AerodromiPraceni();
		ap.setIdent(icao);
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", korisnik).header("zeton",zeton)
				.post(Entity.json(ap));
		
		if (restOdgovor.getStatus() == 200) {
			return true;
		}
		return false;
	}
	
	public static Aerodrom dohvatiAerodrom(String korisnik, String zeton, String icao)
	{
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/aerodromi/"+icao);
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", korisnik).header("zeton",zeton)
				.get();
		
		Aerodrom aerodrom = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodrom = gson.fromJson(odgovor, Aerodrom.class);
		}
		return aerodrom;
	}
	
	public static int dohvatiBrojPracenihAerodroma(String korisnik, String zeton)
	{
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/aerodromi?preuzimanje");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", korisnik).header("zeton",zeton)
				.get();
		
		
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			List<Aerodrom> aerodrom = new ArrayList<>();
			aerodrom.addAll(Arrays.asList(gson.fromJson(odgovor, Aerodrom[].class)));
			return aerodrom.size();
		}
		return -1;
	}
	
	/**
     * Pretvori datum U sekunde.
     *
     * @param datum datum
     * @return int
     * @throws ParseException parse exception
     */
	public static long pretvoriDatumUSekunde(String datum) {
		String formatBezSati = "[0-3]\\d\\.[0-1]\\d\\.\\d{4}";
		String formatSaSatima = "[0-3]\\d\\.[0-1]\\d\\.\\d{4} [0-2]\\d:[0-6]\\d:[0-6]\\d";
		SimpleDateFormat sdf = null;
		if(datum.matches(formatBezSati))
			sdf = new SimpleDateFormat("dd.MM.yyyy");
		else if(datum.matches(formatSaSatima))
			sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
       
        Date date = null;
        if (sdf == null)
        	return -1;
		try {
			date = sdf.parse(datum);
		} catch (ParseException e) {
			return -1;
		}
		if(date != null)
			return date.getTime()/1000;
		else 
			return -1;
    }
}
