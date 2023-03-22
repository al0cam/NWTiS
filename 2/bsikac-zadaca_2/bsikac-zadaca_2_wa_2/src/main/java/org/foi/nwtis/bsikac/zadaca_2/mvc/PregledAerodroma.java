package org.foi.nwtis.bsikac.zadaca_2.mvc;

import java.util.List;

import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
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
@Path("/aerodromi")
@RequestScoped
public class PregledAerodroma {
	
	/** model. */
	@Inject 
	private Models model;
	
	/**
	 * Pocetak.
	 */
	@GET
	@Path("/pocetak")
	@View("index.jsp")
	public void pocetak() {
	}
	
	/**
	 * Pregled svih aerodroma.
	 */
	@GET
	@Path("/pregledSvihAerodroma")
	@View("pregledSvihAerodroma.jsp")
	public void pregledSvihAerodroma() {
		AerodromiKlijent ak = new AerodromiKlijent();
		List<Aerodrom> a =  ak.dajSveAerodrome();
		model.put("aerodromi", a);
	}
	
	/**
	 * Pregled aerodroma.
	 */
	@GET
	@Path("/pregledAerodroma")
	@View("pregledAerodroma.jsp")
	public void pregledAerodroma() {
		AerodromiKlijent ak = new AerodromiKlijent();
		Aerodrom a = null;
	}
	
	/**
	 * Pregled aerodroma icao.
	 *
	 * @param icao icao
	 */
	@POST
	@Path("/pregledAerodroma")
	@View("pregledAerodroma.jsp")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	public void pregledAerodromaIcao(String icao) {
		AerodromiKlijent ak = new AerodromiKlijent();
		Aerodrom a = null;
		if(icao != null)
			a =  ak.dajAerodrom(icao);
		model.put("aerodromi", a);
	}
	
	/**
	 * Pregled pracenih aerodroma.
	 */
	@GET
	@Path("/pregledPracenihAerodroma")
	@View("pregledPracenihAerodroma.jsp")
	public void pregledPracenihAerodroma() {
		AerodromiKlijent ak = new AerodromiKlijent();
		List<Aerodrom> a =  ak.dajPraceneAerodrome();
		model.put("aerodromi", a);
	}
	
	/**
	 * Pregled dolazaka icao na dan.
	 *
	 * @param icao icao
	 * @param datum the datum
	 */
	@POST
	@Path("/pregledDolazakaNaDan")
	@View("pregledDolazakaNaDan.jsp")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	public void pregledDolazakaIcaoNaDan(String icao, String datum) {
		AerodromiKlijent ak = new AerodromiKlijent();
		List<AvionLeti> a = null;
		if(icao != null)
			a =  ak.dajDolaskeZaIcaoNaDatum(icao, datum);
		model.put("aerodromi", a);
	}
	
	/**
	 * Pregled polazaka icao na dan.
	 *
	 * @param icao icao
	 * @param datum datum
	 */
	@POST
	@Path("/pregledPolazakaNaDan")
	@View("pregledPolazakaNaDan.jsp")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	public void pregledPolazakaIcaoNaDan(String icao, String datum) {
		AerodromiKlijent ak = new AerodromiKlijent();
		List<AvionLeti> a = null;
		if(icao != null)
			a =  ak.dajPolaskeZaIcaoNaDatum(icao, datum);
		model.put("aerodromi", a);
	}
	
}
