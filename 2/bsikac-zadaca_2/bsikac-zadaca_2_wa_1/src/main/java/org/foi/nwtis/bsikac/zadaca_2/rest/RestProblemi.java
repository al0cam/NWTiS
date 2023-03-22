package org.foi.nwtis.bsikac.zadaca_2.rest;

import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.bsikac.zadaca_2.modeli.AerodromiPraceni;
import org.foi.nwtis.bsikac.zadaca_2.modeli.AerodromiProblemi;
import org.foi.nwtis.bsikac.zadaca_2.podaci.AerodromPraceniDAO;
import org.foi.nwtis.bsikac.zadaca_2.podaci.AerodromProblemDAO;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestProblemi.
 */
@Path("/problemi")
public class RestProblemi {
	
	/** Kontekst aplikacije. */
	@Inject
	ServletContext context;
	
	/**
	 * Daj sve probleme.
	 *
	 * @return response
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajSveProbleme() {
		Response odgovor = null;
		AerodromProblemDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		AerodromiProblemi[] ai = AerodromProblemDAO.dohvatiSveAerodromProbleme();
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
	 * Daj Sve Probleme Za Icao.
	 *
	 * @param icao icao
	 * @return response
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{icao}")
	public Response dajSveProblemeZaIcao(@PathParam("icao") String icao) {
		Response odgovor = null;
		AerodromProblemDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		AerodromiProblemi[] ai = AerodromProblemDAO.dohvatiSveAerodromProblemeZaIcao(icao);
		
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
	 * Obiris problem za icao.
	 *
	 * @param icao icao
	 * @return response
	 */
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{icao}")
	public Response obirisAerodromProblem(@PathParam("icao") String icao) {
		Response odgovor = null;
		AerodromProblemDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		System.out.println(icao);
		Boolean ai = AerodromProblemDAO.obirisAerodromProblem(icao);
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
