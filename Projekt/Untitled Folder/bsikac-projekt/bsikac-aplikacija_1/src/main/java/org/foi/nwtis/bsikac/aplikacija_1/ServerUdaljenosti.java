package org.foi.nwtis.bsikac.aplikacija_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.foi.nwtis.bsikac.vjezba_03.konfiguracije.Konfiguracija;
import org.foi.nwtis.bsikac.vjezba_03.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.bsikac.vjezba_03.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.podaci.Aerodrom;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.NoArgsConstructor;

/**
 * Klasa ServerUdaljenosti.
 */

@NoArgsConstructor
public class ServerUdaljenosti {
	
	/** Broj porta. */
	private int port = 0;
	
	private int brojDretvi;
	
	private ServerSocket serverSocket;
	
	/** Podaci o aerodromima. */
	private ArrayList<Aerodrom> aeroPodaci;

	/** Veza na mrežnu utičnicu. */
	private Socket veza = null;

	/** Konfiguracijski podaci. */
	private static Konfiguracija konfig = null;
	
	//	0 - hiberniran, 1 - inicijaliziran, 2 - aktivan
	private static int status = 0;
	
//	0 - radi, != 0 - ugasi posluzitelj
	private static boolean prekidac = false;
	

	/**
	 * Početna metoda.
	 *
	 * @param args the argumenti
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("ERROR 14 Parametar mora biti naziv konfiguracijske datoteke!");
			return;
		}

		if (!ucitajKonfiguraciju(args[0]))
			return;

		if (!konfiguracijaSadrzi("port"))
			return;
		
		if (!konfiguracijaSadrzi("brojDretvi"))
			return;

		int port = Integer.parseInt(konfig.dajPostavku("port"));
		if (!portSlobodan(port))
			return;

		int brojDretvi = Integer.parseInt(konfig.dajPostavku("brojDretvi"));

		ServerUdaljenosti su = new ServerUdaljenosti();
		su.port = port;
		su.brojDretvi = brojDretvi;

		System.out.println("Server se podiže na portu: " + port);
		su.obradaZahtjeva();
	}


	/**
	 * Ucitaj konfiguraciju.
	 *
	 * @param nazivDatoteke naziv datoteke
	 * @return true, ako je uspješno
	 */
	private static boolean ucitajKonfiguraciju(String nazivDatoteke) {
		try {
			konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
		} catch (NeispravnaKonfiguracija e) {
			System.out.println("ERROR 14 Došlo je do pogreške prilikom učitavanja konfiguracije!");
			return false;
		}
		return true;
	}

	/**
	 * Obrada zahtjeva.
	 */
	public void obradaZahtjeva() {
		try (ServerSocket ss = new ServerSocket(this.port, this.brojDretvi))
		{
			serverSocket = ss;
			while (!prekidac) {
				this.veza = ss.accept();
				DretvaObrade dretvaObrade = new DretvaObrade(veza);
				dretvaObrade.start();
			}
		} catch (IOException ex) {
			if(!prekidac)
				System.out.println("ERROR 14 došlo je do problema s otvaranjem server socketa");
		}

	}

	/**
	 * Obradi naredbu je metoda koja se koristi za oredivanje koja ce se metoda dalje koristiti u izvrsavanju programa.
	 *
	 * @param zahtjev 	zahtjev.
	 * @return 			string.
	 */
	private String obradiNaredbu(String zahtjev) {
		Pattern pDist = Pattern.compile("^DISTANCE ([A-Z]{4}) ([A-Z]{4})$"),
				pClear = Pattern.compile("^CLEAR$"),
				pStatus = Pattern.compile("^STATUS$"),
				pQuit = Pattern.compile("^QUIT$"),
				pInit = Pattern.compile("^INIT$"),
				pLoad = Pattern.compile("^LOAD (?<podaci>\\[\\{.+\\}\\])$");

		Matcher mDist = pDist.matcher(zahtjev), 
				mClear = pClear.matcher(zahtjev),
				mStatus = pStatus.matcher(zahtjev),
				mQuit = pQuit.matcher(zahtjev),
				mInit = pInit.matcher(zahtjev),
				mLoad = pLoad.matcher(zahtjev);

		String odgovor = "ERROR 14 Format komande nije ispravan";
		System.out.println(zahtjev);

		if (mDist.matches()) {
			odgovor = izvrsiNaredbuDist(zahtjev);
		} else if (mClear.matches()) {
			odgovor = izvrsiNaredbuClear();
		}else if (mStatus.matches()) {
			odgovor = izvrsiNaredbuStatus();
		}else if (mQuit.matches()) {
			odgovor = izvrsiNaredbuQuit();
		}else if (mInit.matches()) {
			odgovor = izvrsiNaredbuInit();
		}else if (mLoad.matches()) {
			odgovor = izvrsiNaredbuLoad(mLoad.group("podaci"));
		}

		return odgovor;
	}

	/**
	 * Izvrsi naredbu dist je metoda koja racuna udaljenost dva {@link Aerodrom},
	 * ako se ne nalaze u lokalnom spremniku {@link #aeroPodaci} onda se dohvacaju sa servera {@link ServerAerodroma}.
	 *
	 * @param zahtjev 	zahtjev.
	 * @return  		string.
	 */
	private String izvrsiNaredbuDist(String zahtjev) {
		if(status == 0)
			return "ERROR 01 poslužitelj je hiberniran";
		else if (status == 1)
			return "ERROR 02 poslužitelj je inicijaliziran";
		
		String[] podaci = zahtjev.split(" ");
		String icao1 = podaci[1], icao2 = podaci[2];
		Aerodrom aero1 = null, aero2 = null;
		for (Aerodrom a : aeroPodaci) {
			if (a.getIcao().equals(icao1)) {
				aero1 = a;
			} else if (a.getIcao().equals(icao2)) {
				aero2 = a;
			}
		}
		if (aero1 == null && aero2 == null)
			return "ERROR 13 oba aerodroma nisu učitana";
		else if(aero1 == null)
			return "ERROR 11 nije učitan prvi aerodrom";
		else if(aero2 == null)
			return "ERROR 12 nije učitan drugi aerodrom";

		return "OK " + (int) udaljenostDvijeTockeNaSferi(aero1, aero2);
	}
	
	
	private String izvrsiNaredbuStatus() {
		return "OK " + status;
	}
	
	private String izvrsiNaredbuQuit() {
		prekidac = true;
		try {
			serverSocket.close();
		} catch (IOException e) {
		}
		return "OK";
	}
	
	private String izvrsiNaredbuInit() {
		if(status == 1)
			return "ERROR 02 poslužitelj je već inicijaliziran";
		else if (status == 2)
			return "ERROR 03 poslužitelj je već aktivan";
		status = 1;
		return "OK";
	}
	
	private String izvrsiNaredbuLoad(String podaci) {
		if(status == 0)
			return "ERROR 01 poslužitelj je hiberniran";
		else if (status == 2)
			return "ERROR 03 poslužitelj je već aktivan";
		
		Gson g = new Gson();
		aeroPodaci =  g.fromJson(podaci, new TypeToken<List<Aerodrom>>() {}.getType());
		status = 2;
		
		return "OK" + " "+aeroPodaci.size();
	}
	

	/**
	 * Izvrsi naredbu dist clear je metoda koja cisti {@see #aeroPodaci}
	 *
	 * @return the 		string.
	 */
	private String izvrsiNaredbuClear() {
		if (status == 0)
			return "ERROR 01 poslužitelj je hiberniran";
		else if (status == 1)
			return "ERROR 02 poslužitelj je inicijaliziran";
		aeroPodaci.clear();
		status = 0;
		return "OK";
	}
	
	/**
	 * Metoda pretvara string u objekt tipa {@see Aerodrom}
	 *
	 * @param odgovor 	string koji sadrzi podatke o aerodromu.
	 * @return 			{@see Aerodrom}.
	 */
//	private Aerodrom stringUAerodrom(String odgovor) {
//		String[] podaci = odgovor.split("\""), statusNaziv = podaci[0].split(" "), koordinate = podaci[2].split(" ");
//		String naziv = podaci[1];
//		return new Aerodrom(statusNaziv[1], naziv, koordinate[1], koordinate[2].replace(";", ""));
//	}

	

	/**
	 * Konfiguracija sadrzi je metoda koja provjerava sadri li konfiguracija odredeni parametar.
	 *
	 * @param kljuc 	je ime parametra.
	 * @return true, 	ako konfiguracija sadrzi parametar.
	 */
	private static boolean konfiguracijaSadrzi(String kljuc) {
		if (konfig.dajPostavku(kljuc) == null || konfig.dajPostavku(kljuc).isEmpty()) {
			System.out.println("ERROR 14 " + kljuc + " nije definiran u konfiguraciji!");
			return false;
		}
		return true;

	}

	/**
	 * Port slobodan je metoda koja provjerava je li odredeni port slobodan.
	 *
	 * @param port 		broj porta.
	 * @return true, 	ako je slobodan.
	 */
	private static boolean portSlobodan(int port) {
		ServerSocket skt;
		try {
			skt = new ServerSocket(port);
			skt.close();
		} catch (IOException e) {
			System.out.println("ERROR 14 Port se vec koristi!");
			return false;
		}
		return true;

	}

	/**
	 * Klasa DretvaObrade koja se koristi za ostvarivanje visedretvenog rada.
	 */
	public class DretvaObrade extends Thread {
		
		/** Veza na mrežnu utičnicu. */
		private Socket vezaD = null;

		/**
		 * Konstruktor klase {@see DretvaObrade}.
		 *
		 * @param veza 	veza.
		 */
		public DretvaObrade(Socket veza) {
			super();
			this.vezaD = veza;
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
			super.run();
			try (InputStreamReader isr = new InputStreamReader(this.vezaD.getInputStream(), Charset.forName("UTF-8"));
					OutputStreamWriter osw = new OutputStreamWriter(this.vezaD.getOutputStream(),
							Charset.forName("UTF-8"));) {

				StringBuilder tekst = new StringBuilder();
				while (true){

					int i = isr.read();
					if (i == -1) {
						break;
					}
					tekst.append((char) i);
				}

				this.vezaD.shutdownInput();

				String odgovor = obradiNaredbu(tekst.toString());
				osw.write(odgovor);
				osw.flush();
				this.vezaD.shutdownOutput();
//			
			} catch (IOException e) {
				System.out.println("ERROR 14 došlo je do pogreške prilikom čitanja poruke");
			}
		}

		/**
		 * Prekid.
		 */
		@Override
		public void interrupt() {
			prekidac = true;
			super.interrupt();
		}

	}

	/**
	 * Udaljenost dvije tocke na sferi.
	 *
	 * @param lokacija  lokacija
	 * @param icao2  icao 2
	 * @return  double
	 */
	static double udaljenostDvijeTockeNaSferi(Aerodrom icao1, Aerodrom icao2) {
		
		double dLat = Math.toRadians(
				Double.parseDouble(icao2.getLokacija().getLatitude()) - Double.parseDouble(icao1.getLokacija().getLatitude()));
		double dLon = Math.toRadians(
				Double.parseDouble(icao2.getLokacija().getLongitude()) - Double.parseDouble(icao1.getLokacija().getLongitude()));

		double gs1 = Math.toRadians(Double.parseDouble(icao1.getLokacija().getLatitude()));
		double gs2 = Math.toRadians(Double.parseDouble(icao2.getLokacija().getLatitude()));

		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(gs1) * Math.cos(gs2);
		double radiusZemlje = 6371;
		double c = 2 * Math.asin(Math.sqrt(a));
		return zaokruzi(radiusZemlje * c);
	}

	

	/**
	 * Ispis je metoda koja ispisuje na ekran.
	 *
	 * @param message  message.
	 */
	private void ispis(String message) {
		System.out.println(message);
	}

	/**
	 * Zaokruzi zaokruzuje broj na jednu decimalu.
	 *
	 * @param broj 	broj.
	 * @return 		double.
	 */
	private static double zaokruzi(double broj) {
		return Math.round(broj);
	}

}
