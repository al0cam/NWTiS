package org.foi.nwtis.bsikac.zadaca_2.rest;


import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.bsikac.zadaca_2.modeli.AerodromiPraceni;
import org.foi.nwtis.bsikac.zadaca_2.modeli.AerodromiProblemi;
import org.foi.nwtis.bsikac.zadaca_2.podaci.AerodromDAO;
import org.foi.nwtis.bsikac.zadaca_2.podaci.AerodromDolasciDAO;
import org.foi.nwtis.bsikac.zadaca_2.podaci.AerodromPolasciDAO;
import org.foi.nwtis.bsikac.zadaca_2.podaci.AerodromPraceniDAO;
import org.foi.nwtis.bsikac.zadaca_2.podaci.AerodromProblemDAO;
import org.foi.nwtis.bsikac.zadaca_2.podaci.AirportDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestAerodromi.
 */
@Path("/aerodromi")
public class RestAerodromi {
	
	/** Kontekst aplikacije. */
	@Inject
	ServletContext context;
	
	
	
	/**
	 * Daj metoda.
	 *
	 * @param hr hr
	 * @return response
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajMetoda(@Context HttpServletRequest hr)
	{
		
		Response odgovor = null;
		if(hr.getQueryString()!=null && hr.getQueryString().equals("preuzimanje"))
			odgovor = dajPraceneAerodrome();
		else
			odgovor = dajSveAerodrome();
		
		return odgovor;
	}
	
	
	/**
	 * Daj sve aerodrome.
	 *
	 * @return response
	 */
	public Response dajSveAerodrome() {
		Response odgovor = null;
		
		AerodromDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Aerodrom[] ai = AerodromDAO.dohvatiSveAerodrome();
		if(ai != null)
		{
			odgovor = Response.status(Response.Status.OK).entity(ai).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podataka").build();
		}
		return odgovor;
	}
	
	/**
	 * Dodaj aerodrom za pratiti.
	 *
	 * @param icao icao
	 * @return response
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response dodajAerodromZaPratiti(AerodromiPraceni icao) {
		Response odgovor = null;
		AerodromPraceniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		System.out.println("ICAO:"+ icao);
		System.out.println("UCAO: "+icao.getIdent());
		AerodromiPraceni ap = new AerodromiPraceni();
		ap = icao;
//		ap.setIdent(icao.);
		Boolean ai = AerodromPraceniDAO.dodajAerodromPraceni(ap);
		if(ai != null)
		{
			odgovor = Response.status(Response.Status.OK).entity(ai).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podataka").build();
		}
		return odgovor;
		
	}
	
	/**
	 * Daj aerodrom za icao.
	 *
	 * @param icao icao
	 * @return response
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{icao}")
	public Response dajAerodrom(@PathParam("icao") String icao) {
		Response odgovor = null;
		AerodromDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Aerodrom ai = AerodromDAO.dohvatiAerodrom(icao);
		
		if(ai != null)
		{
			odgovor = Response.status(Response.Status.OK).entity(ai).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podataka").build();
		}
		return odgovor;
		
	}
	
	/**
	 * Daj polaske aerodoma za icao.
	 *
	 * @param icao icao
	 * @param datum datum
	 * @return response
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{icao}/polasci")
	public Response dajPolaskeAerodoma(@PathParam("icao") String icao, @QueryParam("dan") String datum) {
		Response odgovor = null;
		AerodromDolasciDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		AvionLeti[] ai = AerodromPolasciDAO.dohvatiSveAerodromPolaskeNaDan(datum);
		if(ai != null)
		{
			odgovor = Response.status(Response.Status.OK).entity(ai).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podataka").build();
		}
		return odgovor;
		
	}
	
	/**
	 * Daj dolaske aerodoma za icao.
	 *
	 * @param icao icaom
	 * @param datum datum
	 * @return response
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{icao}/dolasci")
	public Response dajDolaskeAerodoma(@PathParam("icao") String icao, @QueryParam("dan") String datum) {
		Response odgovor = null;
		AerodromPolasciDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		AvionLeti[] ai = AerodromDolasciDAO.dohvatiSveAerodromDolaskeNaDan(datum);
		
		if(ai != null)
		{
			odgovor = Response.status(Response.Status.OK).entity(ai).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podataka").build();
		}
		return odgovor;
		
	}
	
	/**
	 * Daj pracene aerodrome.
	 *
	 * @return response
	 */
	private Response dajPraceneAerodrome() {
		Response odgovor = null;
		AerodromPraceniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		AerodromiPraceni[] ai = AerodromPraceniDAO.dohvatiSveAerodromePracene();
		
		if(ai != null)
		{
			odgovor = Response.status(Response.Status.OK).entity(ai).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podataka").build();
		}
		return odgovor;
	}
	
}
