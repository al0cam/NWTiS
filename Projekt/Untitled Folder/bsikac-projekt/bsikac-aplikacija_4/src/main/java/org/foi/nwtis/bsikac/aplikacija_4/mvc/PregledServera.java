package org.foi.nwtis.bsikac.aplikacija_4.mvc;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.foi.nwtis.bsikac.aplikacija_4.modeli.Zeton;
import org.foi.nwtis.bsikac.aplikacija_4.transfer.KorisniciKlijent;
import org.foi.nwtis.bsikac.aplikacija_4.transfer.ServerKlijent;
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


@Controller
@Path("/server")
@RequestScoped
public class PregledServera {

	/** model. */
	@Inject
	private Models model;

	@GET
	@View("server.jsp")
	public void server() {
		Map<String, String> map = new TreeMap();
		map.put("INIT", "Inicijalizacija posluzitelja");
		map.put("QUIT", "Prekid rada posluzitelja");
		map.put("LOAD", "Ucitavanje podataka");
		map.put("CLEAR", "Brisanje podataka");
		model.put("map", map);
		String status = ServerKlijent.posaljiZahtjev("STATUS");
		if(status == null)
			model.put("status", "posluzitelj nije pokrenut");
		else
			model.put("status", status);
	}
	
	@POST
	@View("server.jsp")
	public void serverSaljeKomandu(@FormParam("opcija") String opcija) {
		Map<String, String> map = new TreeMap();
		map.put("INIT", "Inicijalizacija posluzitelja");
		map.put("QUIT", "Prekid rada posluzitelja");
		map.put("LOAD", "Ucitavanje podataka");
		map.put("CLEAR", "Brisanje podataka");
		model.put("map", map);
		String rezulatKomande = ServerKlijent.posaljiZahtjev(opcija);
		String status = ServerKlijent.posaljiZahtjev("STATUS");
		if(status == null)
			model.put("status", "posluzitelj nije pokrenut");
		else
			model.put("status", status);
		if(rezulatKomande == null)
			model.put("rezultatKomande", "neuspjesno");
		else
			model.put("rezultatKomande", rezulatKomande);
	}
	
}
