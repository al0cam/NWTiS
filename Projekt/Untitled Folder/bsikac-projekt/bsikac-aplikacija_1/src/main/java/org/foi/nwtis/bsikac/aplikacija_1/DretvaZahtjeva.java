package org.foi.nwtis.bsikac.aplikacija_1;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.foi.nwtis.bsikac.vjezba_03.konfiguracije.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.Lokacija;

import com.google.gson.Gson;

import lombok.NoArgsConstructor;

/**
 * Klasa DretvaZahtjeva.
 */
@NoArgsConstructor
public class DretvaZahtjeva extends Thread {
	
	
	public static void main(String[] args)
	{
		DretvaZahtjeva dr = new DretvaZahtjeva();
		dr.start();
	}

	/** Veza na mrežnu utičnicu. */
	private Socket veza = null;
	
	/** Konfiguracijski podaci. */
	private Konfiguracija konfig = null;
	
	/** Broj dretve. */
	private int brojDretve;
	
	/** The ime dretve. */
	private String ime = "bsikac_";

	/**
	 * Konstruktor klase {@link DretvaZahtjeva}.
	 *
	 * @param veza 			veza.
	 * @param konfig 		konfig.
	 * @param brojDretve 	broj dretve.
	 */
	public DretvaZahtjeva(Socket veza, Konfiguracija konfig, int brojDretve) {
		super();
		this.veza = veza;
		this.konfig = konfig;
		this.brojDretve = brojDretve;
		setName(ime + brojDretve);
	}

	/**
	 * Start.
	 */
	@Override
	public synchronized void start() {
		super.start();
	}

	/**
	 * Run.
	 */
	@Override
	public void run() {
		Aerodrom a  = new Aerodrom();
		a.setDrzava("CRO");
		a.setIcao("LDZA");
		a.setNaziv("Zagreb");
		a.setLokacija(new Lokacija("45.7429008484","16.0687999725"));
		Aerodrom a2  = new Aerodrom();
		a2.setDrzava("CRO");
		a2.setIcao("LDVA");
		a2.setNaziv("Zagreb");
		a2.setLokacija(new Lokacija("48.1102981567","16.569700241089"));
	
		List<Aerodrom> au = new ArrayList<Aerodrom>();
		au.add(a2);
		au.add(a);
		Gson gson = new Gson();
		String loader = gson.toJson(au, List.class);
		System.out.println(loader);
		
		posaljiKomandu("localhost", 8000, "INIT");
		String val = posaljiKomandu("localhost", 8000, "LOAD "+loader);
//		String val = posaljiKomandu("localhost", 8000, "INIT");
		

		String valr = posaljiKomandu("localhost", 8000, "DISTANCE LDZA LDVA");
		String mor = posaljiKomandu("localhost", 8000, "QUIT");
		
		
		
		System.out.println(val);
		System.out.println(valr);
		System.out.println(mor);
	}

	/**
	 * Prekid.
	 */
	@Override
	public void interrupt() {
		// TODO Auto-generated method stub
		super.interrupt();
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
			return "ERROR 49 vrijeme cekanja na odgovor je isteklo";
		}
		return komanda;
		
		
	}
	
	

}
