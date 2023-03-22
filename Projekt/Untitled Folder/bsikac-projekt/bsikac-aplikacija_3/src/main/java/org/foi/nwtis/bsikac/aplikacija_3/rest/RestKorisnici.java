package org.foi.nwtis.bsikac.aplikacija_3.rest;

import org.foi.nwtis.bsikac.aplikacija_3.modeli.Grupa;
import org.foi.nwtis.bsikac.aplikacija_3.modeli.Zeton;
import org.foi.nwtis.bsikac.aplikacija_3.podaci.KorisniciDAO;
import org.foi.nwtis.bsikac.aplikacija_3.podaci.ZetoniDAO;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Korisnik;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
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
@Path("/korisnici")
public class RestKorisnici {
	
	/** Kontekst aplikacije. */
	@Inject
	ServletContext context;
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajSveKorisnike(@HeaderParam("korisnik") String korisnik, @HeaderParam("zeton") String zeton) {
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnik))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();
		
		
		Response odgovor = null;
		KorisniciDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Korisnik[] ai = KorisniciDAO.dohvatiKorisnike();
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
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response dodajKorisnika(Korisnik korisnik, @HeaderParam("korisnik") String korisnikHP, @HeaderParam("zeton") String zeton) {
		
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnikHP))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();

		Response odgovor = null;
		if(korisnik.getKorIme().equals("") || korisnik.getEmail().equals("") || korisnik.getIme().equals("") || korisnik.getLozinka().equals("") || korisnik.getPrezime().equals(""))
			return odgovor;
		
		KorisniciDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		boolean ai = KorisniciDAO.dodajKorisnika(korisnik);
		if(ai)
		{
			odgovor = Response.status(Response.Status.OK).entity(ai).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nije_moguce_dodati_korisnika").build();
		}
		return odgovor;
		
	}
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{korisnik}")
	public Response dajKorisnika(@PathParam("korisnik") String korisnik, @HeaderParam("korisnik") String korisnikHP, @HeaderParam("zeton") String zeton) {
		
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnikHP))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();
		
		
		Response odgovor = null;
		KorisniciDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Korisnik ai = KorisniciDAO.dohvatiKorisnika(korisnik);
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

	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{korisnik}/grupe")
	public Response dajGrupeKorisnika(@PathParam("korisnik") String korisnik, @HeaderParam("korisnik") String korisnikHP, @HeaderParam("zeton") String zeton) {
		
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnikHP))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();
		
		
		Response odgovor = null;
		KorisniciDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Korisnik ai = KorisniciDAO.dohvatiKorisnika(korisnik);
		Grupa[] g = KorisniciDAO.dohvatiGrupeZaKorisnika(ai);
		if(g != null)
		{
			odgovor = Response.status(Response.Status.OK).entity(g).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema podataka").build();
		}
		return odgovor;
	}
	
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{korisnik}/grupe/admin")
	public Response korisnikAdmin(@PathParam("korisnik") String korisnik, @HeaderParam("korisnik") String korisnikHP, @HeaderParam("zeton") String zeton) {
		
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnikHP))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();
		
		String grupa = (String) context.getAttribute("sustav.administratori");
		boolean admin = false;
		Response odgovor = null;
		KorisniciDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Korisnik ai = KorisniciDAO.dohvatiKorisnika(korisnik);
		Grupa[] g = KorisniciDAO.dohvatiGrupeZaKorisnika(ai);
		for(Grupa gr : g)
		{
			if(gr.getGrupa().equals(grupa))
			{
				admin = true;
				break;
			}
		}
		if(admin)
		{
			odgovor = Response.status(Response.Status.OK).entity(admin).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.NOT_FOUND).entity(admin).build();
		}
		return odgovor;
	}
}
