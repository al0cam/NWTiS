package org.foi.nwtis.bsikac.aplikacija_4.transfer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.bsikac.aplikacija_4.modeli.Zeton;
import org.foi.nwtis.bsikac.aplikacija_4.mvc.MVC;
import org.foi.nwtis.bsikac.aplikacija_4.slusaci.SlusacAplikacije;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.podaci.AvionLeti;

import com.google.gson.Gson;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

/**
 * Klasa AerodromiKlijent.
 */
public class ServerKlijent {
	
	private static PostavkeBazaPodataka dbKonf = null;
	private static String adresa = null;
	private static int port = 0;
	
	
	public static String posaljiZahtjev(String komanda) {
		postaviDbKonf();
		if(komanda.equals("LOAD"))
		{
			if(MVC.korisnickiZeton == null)
				return "potrebno se ulogirati za ucitavnje podataka";
			List<Aerodrom> a = dohvatiAerodromePracene();
			Gson gson = new Gson();
			String json = gson.toJson(a, List.class);
			System.out.println("JSON: "+json);
			return posaljiKomandu(adresa, port, komanda + " "+json);
		}
		else
			return posaljiKomandu(adresa, port, komanda);
	}
	
	
	public static List<Aerodrom> dohvatiAerodromePracene()
	{
		postaviDbKonf();
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8080/bsikac-aplikacija_3/api/aerodromi?preuzimanje");
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json").header("korisnik", MVC.korisnickiZeton.getKorisnik()).header("zeton",MVC.korisnickiZeton.getZeton())
				.get();
		
		List<Aerodrom> aerodrom = null;
		
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodrom = new ArrayList<>();
			aerodrom.addAll(Arrays.asList(gson.fromJson(odgovor, Aerodrom[].class)));
		}
		return aerodrom;
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
	public static String posaljiKomandu(String adresa, int port, String komanda) {
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
		return null;
	}
	
	public static void postaviDbKonf()
	{
		if(dbKonf == null)
		{
			dbKonf = SlusacAplikacije.getDbKonf();
			adresa = dbKonf.dajPostavku("adresa.aplikacija_1");
			port = Integer.parseInt(dbKonf.dajPostavku("port.aplikacija_1"));
		}
	}
}
