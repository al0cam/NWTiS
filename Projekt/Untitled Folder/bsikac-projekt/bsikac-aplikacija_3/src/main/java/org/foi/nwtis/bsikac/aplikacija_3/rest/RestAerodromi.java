package org.foi.nwtis.bsikac.aplikacija_3.rest;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.foi.nwtis.bsikac.aplikacija_3.modeli.AerodromiPraceni;
import org.foi.nwtis.bsikac.aplikacija_3.modeli.Zeton;
import org.foi.nwtis.bsikac.aplikacija_3.podaci.AerodromDAO;
import org.foi.nwtis.bsikac.aplikacija_3.podaci.AerodromDolasciDAO;
import org.foi.nwtis.bsikac.aplikacija_3.podaci.AerodromPolasciDAO;
import org.foi.nwtis.bsikac.aplikacija_3.podaci.AerodromPraceniDAO;
import org.foi.nwtis.bsikac.aplikacija_3.podaci.ZetoniDAO;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
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
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response dajMetoda(@Context HttpServletRequest hr, @HeaderParam("korisnik") String korisnikHP, @HeaderParam("zeton") String zeton)
	{
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnikHP))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();
		
		Response odgovor = null;
		if(hr.getQueryString()!=null && hr.getQueryString().equals("preuzimanje"))
			odgovor = dajPraceneAerodrome();
		else
			odgovor = dajSveAerodrome();
		
		return odgovor;
	}
	
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
	public Response dodajAerodromZaPratiti(AerodromiPraceni icao, @HeaderParam("korisnik") String korisnikHP, @HeaderParam("zeton") String zeton) {
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnikHP))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();
		
		Response odgovor = null;
		AerodromPraceniDAO.setDbKonf((PostavkeBazaPodataka) context.getAttribute("postavke"));
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
	public Response dajAerodrom(@PathParam("icao") String icao, @HeaderParam("korisnik") String korisnikHP, @HeaderParam("zeton") String zeton) {
		
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnikHP))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();
		
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
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{icao}/dolasci")
	public Response dajDolaskeAerodomaZaVrijeme(@PathParam("icao") String icao, @QueryParam("vrsta") int vrsta,  @QueryParam("vrijemeOd") String vrijemeOd,  @QueryParam("vrijemeDo") String vrijemeDo, @HeaderParam("korisnik") String korisnik, @HeaderParam("zeton") String zeton) {
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnik))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();
		
		long vriOd = 0;
		long vriDo = 0;
		if(vrsta == 0)
		{
			vriOd = pretvoriDatumUSekunde(vrijemeOd);
			vriDo = pretvoriDatumUSekunde(vrijemeDo);
		}
		else 
		{
			vriOd = Long.parseLong(vrijemeOd);
			vriDo = Long.parseLong(vrijemeDo);
		}
			
		Response odgovor = null;
		AerodromDolasciDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		AvionLeti[] ai = AerodromDolasciDAO.dohvatiAerodromDolaskZaIcaoUIntervalu(icao, vriOd, vriDo);
		
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
	@Path("/{icao}/polasci")
	public Response dajPolaskeAerodomaZaVrijeme(@PathParam("icao") String icao, @QueryParam("vrsta") int vrsta,  @QueryParam("vrijemeOd") String vrijemeOd,  @QueryParam("vrijemeDo") String vrijemeDo, @HeaderParam("korisnik") String korisnik, @HeaderParam("zeton") String zeton) {
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnik))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();
		
		long vriOd = 0;
		long vriDo = 0;
		if(vrsta == 0)
		{
			vriOd = pretvoriDatumUSekunde(vrijemeOd);
			vriDo = pretvoriDatumUSekunde(vrijemeDo);
			System.out.println("VRIOD: "+vriOd);
			System.out.println("VRIDO: "+vriDo);
		}
		else 
		{
			vriOd = Long.parseLong(vrijemeOd);
			vriDo = Long.parseLong(vrijemeDo);
		}
			
		Response odgovor = null;
		AerodromPolasciDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		AvionLeti[] ai = AerodromPolasciDAO.dohvatiAerodromPolaskZaIcaoUIntervalu(icao, vriOd, vriDo);
		
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
	@Path("/{icao1}/{icao2}")
	public Response dajUdaljenost(@PathParam("icao1") String icao1, @PathParam("icao2") String icao2, @HeaderParam("korisnik") String korisnik, @HeaderParam("zeton") String zeton) {
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnik))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();
		
			
		String adresa = (String) context.getAttribute("adresa");
		int port = (int) context.getAttribute("port");
		
		Response odgovor = null;
		String komanda = "DISTANCE "+icao1+" "+icao2;
		String ai = posaljiKomandu(adresa, port, komanda);
		if(ai.contains("OK"))
		{
			String[] a = ai.split(" ");
			String ud = "{udaljenost: "+a[1]+"}";
			odgovor = Response.status(Response.Status.OK).entity(ud).build();
		}
		else
		{
			if(ai.equals(""))
				odgovor = Response.status(Response.Status.NOT_FOUND).entity("Server nije pokrenut").build();
			else
				odgovor = Response.status(Response.Status.NOT_FOUND).entity(ai).build();
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
		Aerodrom[] ai = AerodromPraceniDAO.dohvatiSveAerodromePracene();
		
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
     * Pretvori datum U sekunde.
     *
     * @param datum datum
     * @return int
     * @throws ParseException parse exception
     */
	public static long pretvoriDatumUSekunde(String datum) {
		String formatBezSati = "[0-3]\\d\\.[0-1]\\d\\.\\d{4}";
		String formatSaSatima = "[0-3]\\d\\.[0-1]\\d\\.\\d{4} [0-2]\\d:[0-6]\\d:[0-6]\\d";
		SimpleDateFormat sdf = null;
		if(datum.matches(formatBezSati))
			sdf = new SimpleDateFormat("dd.MM.yyyy");
		else if(datum.matches(formatSaSatima))
			sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
       
        Date date = null;
		try {
			date = sdf.parse(datum);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return date.getTime()/1000;
    }
	
	
	/**
	 * Posalji komandu salje komandu na odredenu adresu i port.
	 *
	 * @param adresa 	adresa.
	 * @param port 		port.
	 * @param komanda 	komanda.
	 * @param server 	server.
	 * @return 			string.
	 */
	public String posaljiKomandu(String adresa, int port, String komanda) {
		try (Socket veza = new Socket(adresa, port);
				InputStreamReader isr = new InputStreamReader(veza.getInputStream(), Charset.forName("UTF-8"));
				OutputStreamWriter osw = new OutputStreamWriter(veza.getOutputStream(), Charset.forName("UTF-8"));) {

			osw.write(komanda);
			osw.flush();
			veza.shutdownOutput();
			StringBuilder tekst = new StringBuilder();
			while (true) {
				int i = isr.read();
				if (i == -1) {
					break;
				}
				tekst.append((char) i);
			}
			veza.shutdownInput();
			veza.close();
			return tekst.toString();
		} catch (SocketException e) {
		} catch (IOException ex) {
		}
		return "";
	}
	
}
