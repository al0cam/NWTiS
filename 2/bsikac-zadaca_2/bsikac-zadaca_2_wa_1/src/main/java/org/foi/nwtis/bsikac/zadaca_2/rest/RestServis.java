package org.foi.nwtis.bsikac.zadaca_2.rest;

import java.util.Set;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Klasa RestServis.
 */
@ApplicationPath("/api")
public class RestServis extends Application{
	
	/**
	 * Dodaje sve klase koje imaju putanje za REST.
	 *
	 * @param resources resources
	 */
	private void addRestResourceClasses(Set<Class<?>> resources)  {
		resources.add(org.foi.nwtis.bsikac.zadaca_2.rest.RestAerodromi.class);
		resources.add(org.foi.nwtis.bsikac.zadaca_2.rest.RestProblemi.class);
	}
}
