package org.foi.nwtis.bsikac.aplikacija_3.rest;

import org.foi.nwtis.bsikac.aplikacija_3.modeli.Grupa;
import org.foi.nwtis.bsikac.aplikacija_3.modeli.Zeton;
import org.foi.nwtis.bsikac.aplikacija_3.podaci.KorisniciDAO;
import org.foi.nwtis.bsikac.aplikacija_3.podaci.ZetoniDAO;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Korisnik;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestProblemi.
 */
@Path("/provjere")
public class RestProvjere {
	
	/** Kontekst aplikacije. */
	@Inject
	ServletContext context;
	

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response provjera(@HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka) {
		Response odgovor = null;
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		KorisniciDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		
		Korisnik ai = KorisniciDAO.dohvatiKorisnikaSaSifrom(korisnik, lozinka);
		
		if(ai != null)
		{
			Zeton z = ZetoniDAO.dodajZeton(ai);

			String returnString = "{zeton: "+z.getZeton()+", vrijeme: "+z.getVrijediDo()+"}";
			odgovor = Response.status(Response.Status.OK).entity(returnString).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.UNAUTHORIZED).entity("Korisnik ne postoji").build();
		}
		return odgovor;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{token}")
	public Response provjeraToken(@HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka, @PathParam("token") String token) {
		Response odgovor = null;
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		
		Zeton ze = ZetoniDAO.dohvatiZeton(token);
		
		if(ze != null)
		{
			if(ze.getKorisnik().equals(korisnik))
			{
				if(ze.aktivan())
					odgovor = Response.status(Response.Status.OK).build();
				else
					odgovor = Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
			}
			else
				odgovor = Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();
		}
		else
		{
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Zeton ne postoji").build();
		}
		return odgovor;
	}
	
	
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{token}")
	public Response brisanje(@HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka, @PathParam("token") String token) {
		Response odgovor = null;
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		
		Zeton ze = ZetoniDAO.dohvatiZeton(token);
		
		if(ze != null)
		{
			if(ze.getKorisnik().equals(korisnik))
			{
				if(ze.aktivan())
				{
					ZetoniDAO.deaktivirajZeton(ze);
					odgovor = Response.status(Response.Status.OK).build();
				}
				else
					odgovor = Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
			}
			else
				odgovor = Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();
		}
		else
		{
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Zeton ne postoji").build();
		}
		return odgovor;
	}
	
	
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/korisnik/{korisnik}")
	public Response brisanjeKorisnikovihZetona(@HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka, @PathParam("korisnik") String korisnikPath) {
		Response odgovor = Response.status(Response.Status.NOT_FOUND).entity("Korisnik_nema_aktivnih_zetona").build();
		
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		KorisniciDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		String grupa = (String) context.getAttribute("sustav.administratori");
		
		Korisnik ai = KorisniciDAO.dohvatiKorisnika(korisnik);
		Korisnik kp = KorisniciDAO.dohvatiKorisnika(korisnikPath);
		Grupa[] g = KorisniciDAO.dohvatiGrupeZaKorisnika(ai);
		boolean admin = false;
		
		
		for(Grupa gr : g)
		{
			if(gr.getGrupa().equals(grupa))
			{
				admin = true;
				System.out.println("ADMIN: "+admin);
				break;
			}
		}
		
		Zeton[] ze = ZetoniDAO.dohvatiZetoneZaKorisnika(kp);
		
		if(ze != null)
		{
			if( korisnikPath.equals(korisnik) || admin)
			{
				for(Zeton z: ze)
				{
					if(z.aktivan())
					{
						ZetoniDAO.deaktivirajZeton(z);
						odgovor = Response.status(Response.Status.OK).build();
					}
				}
			}
			else
				odgovor = Response.status(Response.Status.UNAUTHORIZED).entity("Korisnik_nema_ovlastenje_za_brisanje_zetona").build();
				
		}
		else 
			odgovor = null;
		return odgovor;
	}
}
