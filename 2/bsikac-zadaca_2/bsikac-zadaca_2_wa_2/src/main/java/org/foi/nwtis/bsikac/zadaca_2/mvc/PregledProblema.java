package org.foi.nwtis.bsikac.zadaca_2.mvc;

import java.util.List;

import org.foi.nwtis.bsikac.zadaca_2.modeli.AerodromiProblemi;
import org.foi.nwtis.podaci.Aerodrom;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * Klasa PregledProblema.
 */
@Controller
@Path("/problemi")
@RequestScoped
public class PregledProblema {
	
	/** model. */
	@Inject 
	private Models model;
	
	/**
	 * Pregled svih aerodroma.
	 */
	@GET
	@Path("/pregledSvihProblema")
	@View("pregledSvihProblema.jsp")
	public void pregledSvihAerodroma() {
		ProblemiKlijent ak = new ProblemiKlijent();
		List<AerodromiProblemi> a =  ak.dajSveProbleme();
		model.put("problemi", a);
	}
}
