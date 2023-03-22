package org.foi.nwtis.bsikac.aplikacija_3.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.List;

import org.foi.nwtis.bsikac.aplikacija_3.modeli.Zeton;
import org.foi.nwtis.bsikac.aplikacija_3.podaci.ZetoniDAO;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;

import com.google.gson.Gson;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestProblemi.
 */
@Path("/serveri")
public class RestServeri {
	
	/** Kontekst aplikacije. */
	@Inject
	ServletContext context;
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response status(@HeaderParam("korisnik") String korisnik, @HeaderParam("zeton") String zeton) {
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnik))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();

		String adresa = (String) context.getAttribute("adresa");
		int port = (int) context.getAttribute("port");
		
		Response odgovor = null;
		String komanda = "STATUS";
		String ai = posaljiKomandu(adresa, port, komanda);

		String ud = "{adresa: "+adresa+", port: "+port+"}";
		if(ai.contains("OK 0"))
		{
			odgovor = Response.status(001).entity(ud).build();
		}
		else if(ai.contains("OK 1"))
		{
			odgovor = Response.status(002).entity(ud).build();
		}
		else if(ai.contains("OK 2"))
		{
			odgovor = Response.status(003).entity(ud).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.NOT_FOUND).entity(ai).build();
		}
		return odgovor;
		
	}
	

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{komanda}")
	public Response komande(@PathParam("komanda") String komanda, @HeaderParam("korisnik") String korisnik, @HeaderParam("zeton") String zeton) {
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnik))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();

		String adresa = (String) context.getAttribute("adresa");
		int port = (int) context.getAttribute("port");
		
		Response odgovor = null;
		if(!komanda.equals("QUIT") && !komanda.equals("INIT") && !komanda.equals("CLEAR")) 
			return Response.status(Response.Status.BAD_REQUEST).entity("Pogresna komanda").build();
		
		String ai = posaljiKomandu(adresa, port, komanda);

		String ud = "{adresa: "+adresa+", port: "+port+"}";
		if(ai.contains("OK"))
		{
			odgovor = Response.status(Response.Status.OK).entity(ud).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.BAD_REQUEST).entity(ai).build();
		}
		return odgovor;
	}

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/LOAD")
	public Response komandaLoad(List<Aerodrom> aerodromi, @HeaderParam("korisnik") String korisnik, @HeaderParam("zeton") String zeton) {
		ZetoniDAO.setDbKonf((PostavkeBazaPodataka ) context.getAttribute("postavke"));
		Zeton ze = ZetoniDAO.dohvatiZeton(zeton);
		
		if(!ze.aktivan())
			return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Zeton je istekao").build();
		else if (!ze.getKorisnik().equals(korisnik))
			return Response.status(Response.Status.UNAUTHORIZED).entity("Zeton ne pripada korisniku").build();

		String adresa = (String) context.getAttribute("adresa");
		int port = (int) context.getAttribute("port");
		
		Response odgovor = null;
		Gson gson = new Gson();
		String loader = gson.toJson(aerodromi, List.class);
		String komanda = "LOAD " + loader;
		String ai = posaljiKomandu(adresa, port, komanda);

		String ud = "{adresa: "+adresa+", port: "+port+"}";
		if(ai.contains("OK"))
		{
			String[] con = ai.split(" ");
			odgovor = Response.status(Response.Status.OK).entity(con[1]).build();
		}
		else
		{
			odgovor = Response.status(Response.Status.CONFLICT).entity(ai).build();
		}	
		return odgovor;
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
