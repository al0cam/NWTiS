package org.foi.nwtis.bsikac.zadaca_2.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.bsikac.zadaca_2.modeli.AerodromiProblemi;
import org.foi.nwtis.podaci.Aerodrom;

import com.google.gson.Gson;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

// TODO: Auto-generated Javadoc
/**
 * Class ProblemiKlijent.
 */
public class ProblemiKlijent {

	/**
	 * Daj sve probleme.
	 *
	 * @return list
	 */
	public List<AerodromiProblemi> dajSveProbleme() {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-zadaca_2_wa_1/api/problemi");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.get();
		List<AerodromiProblemi> problemi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			problemi = new ArrayList<>();
			problemi.addAll(Arrays.asList(gson.fromJson(odgovor, AerodromiProblemi[].class)));
		}
		return problemi;
	}
	
	/**
	 * Daj probleme za icao.
	 *
	 * @param icao the icao
	 * @return the list
	 */
	public List<AerodromiProblemi> dajProblemeZaIcao(String icao) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-zadaca_2_wa_1/api/problemi").path(icao);
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.get();
		List<AerodromiProblemi> problemi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			problemi = new ArrayList<>();
			problemi.addAll(Arrays.asList(gson.fromJson(odgovor, AerodromiProblemi[].class)));
		}
		return problemi;
	}
	
}
