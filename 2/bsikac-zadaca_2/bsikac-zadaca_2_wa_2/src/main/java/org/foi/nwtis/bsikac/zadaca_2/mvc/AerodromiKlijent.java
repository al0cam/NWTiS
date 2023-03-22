package org.foi.nwtis.bsikac.zadaca_2.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;

import com.google.gson.Gson;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

/**
 * Klasa AerodromiKlijent.
 */
public class AerodromiKlijent {
	
	/**
	 * Daj sve aerodrome.
	 *
	 * @return list
	 */
	public List<Aerodrom> dajSveAerodrome() {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-zadaca_2_wa_1/api/aerodromi");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.get();
		List<Aerodrom> aerodromi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodromi = new ArrayList<>();
			aerodromi.addAll(Arrays.asList(gson.fromJson(odgovor, Aerodrom[].class)));
		}
		return aerodromi;
	}
	
	
	/**
	 * Daj pracene aerodrome.
	 *
	 * @return list
	 */
	public List<Aerodrom> dajPraceneAerodrome() {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-zadaca_2_wa_1/api/aerodromi")
				.queryParam("preuzimanje", null);
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.get();
		List<Aerodrom> aerodromi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodromi = new ArrayList<>();
			aerodromi.addAll(Arrays.asList(gson.fromJson(odgovor, Aerodrom[].class)));
		}
		return aerodromi;
	}
	
	/**
	 * Daj aerodrom.
	 *
	 * @param icao icao
	 * @return aerodrom
	 */
	public Aerodrom dajAerodrom(String icao) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = 
				client.target("http://localhost:8080/bsikac-zadaca_2_wa_1/api/aerodromi").path(icao);
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.get();
		Aerodrom aerodromi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodromi = gson.fromJson(odgovor, Aerodrom.class);
		}
		return aerodromi;
	}
	
	
	/**
	 * Daj dolaske za icao na datum.
	 *
	 * @param icao the icao
	 * @param datum the datum
	 * @return the list
	 */
	public List<AvionLeti> dajDolaskeZaIcaoNaDatum(String icao, String datum) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-zadaca_2_wa_1/api/aerodromi/"+icao+"/polasci").queryParam("dan", datum);
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.get();
		List<AvionLeti> aerodromi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodromi = new ArrayList<>();
			aerodromi.addAll(Arrays.asList(gson.fromJson(odgovor, AvionLeti[].class)));
		}
		return aerodromi;
	}
	
	/**
	 * Daj polaske za icao na datum.
	 *
	 * @param icao the icao
	 * @param datum the datum
	 * @return the list
	 */
	public List<AvionLeti> dajPolaskeZaIcaoNaDatum(String icao, String datum) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-zadaca_2_wa_1/api/aerodromi/"+icao+"/polasci").queryParam("dan", datum);
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.get();
		List<AvionLeti> aerodromi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodromi = new ArrayList<>();
			aerodromi.addAll(Arrays.asList(gson.fromJson(odgovor, AvionLeti[].class)));
		}
		return aerodromi;
	}

}
