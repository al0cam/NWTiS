package org.foi.nwtis.bsikac.zadaca_1;

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


/**
 * Klasa ServerAerodroma.
 */
public class ServerAerodroma {
	
	/** Broj porta. */
	private int port = 0;
	
	/** Maksimalni broj cekaca. */
	private int maksCekaca = -1;
	
	/** Maksimalno cekanje na odgovor od servera. */
	private int maksCekanje = 0;
	
	/** Adresa servera udaljenosti. */
	private String serverUdaljenostiAdresa = "";
	
	/** Port servera udaljenosti. */
	private int serverUdaljenostiPort = 0;
	
	/** Lokalni meduspremnik. */
	private ConcurrentHashMap<String, String> meduspremnik;

	/** Veza na mrežnu utičnicu. */
	private Socket veza = null;
	
	/** Aerodromski podaci. */
	private List<Aerodrom> aeroPodaci = new ArrayList<Aerodrom>();

	/** Konfiguracijski podaci. */
	private static Konfiguracija konfig = null;

	/**
	 * Početna metoda.
	 *
	 * @param args argumenti.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("ERROR 29 Parametar mora biti naziv konfiguracijske datoteke!");
			return;
		}

		if (!ucitajKonfiguraciju(args[0]))
			return;

		// TODO provjeri jesu li sve postavke koje trebaju biti
		if (!konfiguracijaSadrzi("port"))
			return;
		if (!konfiguracijaSadrzi("maks.cekaca"))
			return;
		if (!konfiguracijaSadrzi("maks.cekanje"))
			return;
		if (!konfiguracijaSadrzi("datoteka.aerodroma"))
			return;
		if (!konfiguracijaSadrzi("server.udaljenosti.port"))
			return;
		if (!konfiguracijaSadrzi("server.udaljenosti.adresa"))
			return;

		int port = Integer.parseInt(konfig.dajPostavku("port"));
		if (port < 8000 || port > 9999) {
			System.out.println("ERROR 29 Port: " + port + " nije u dozvoljenom rasponu(8000-9999)");
			return;
		}
		if (!portSlobodan(port))
			return;

		int maksCekaca = Integer.parseInt(konfig.dajPostavku("maks.cekaca"));
		String nazivDatotekeAeroPodataka = konfig.dajPostavku("datoteka.aerodroma");
		int maksCekanje = Integer.parseInt(konfig.dajPostavku("maks.cekanje"));
		String serverUdaljenostiAdresa = konfig.dajPostavku("server.udaljenosti.adresa");
		int serverUdaljenostiPort = Integer.parseInt(konfig.dajPostavku("server.udaljenosti.port"));

		ServerAerodroma sa = new ServerAerodroma(port, maksCekaca, maksCekanje, serverUdaljenostiAdresa,
				serverUdaljenostiPort);
		if (!sa.ucitajAeroPodatke(nazivDatotekeAeroPodataka))
			return;

		System.out.println("Server se podiže na portu: " + port);
		sa.obradaZahtjeva();
	}

	/**
	 * Konstruktor klase server aerodroma.
	 *
	 * @param port 						broj porta.
	 * @param maksCekaca 				maksimalni broj cekaca.
	 * @param maksCekanje 				maksimalno dozvoljeno cekanje na odgovor.
	 * @param serverUdaljenostiAdresa 	adresa servera udaljenosti.
	 * @param serverUdaljenostiPort 	port servera udaljenosti.
	 */
	public ServerAerodroma(int port, int maksCekaca, int maksCekanje, String serverUdaljenostiAdresa,
			int serverUdaljenostiPort) {
		super();
		this.port = port;
		this.maksCekaca = maksCekaca;
		this.maksCekanje = maksCekanje;
		this.serverUdaljenostiAdresa = serverUdaljenostiAdresa;
		this.serverUdaljenostiPort = serverUdaljenostiPort;
		this.meduspremnik = new ConcurrentHashMap<>();
	}

	/**
	 * Ucitaj konfiguraciju.
	 *
	 * @param nazivDatoteke 	naziv datoteke
	 * @return true, 			ako je konfiguracija uspjesno ucitana.
	 */
	public static boolean ucitajKonfiguraciju(String nazivDatoteke) {
		try {
			konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
		} catch (NeispravnaKonfiguracija e) {
			System.out.println("ERROR 29 Došlo je do pogreške prilikom učitavanja konfiguracije!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Ucitaj aero podatke.
	 *
	 * @param nazivDatotekeAeroPodataka  	naziv datoteke aerodromskih podataka.
	 * @return true, 						ako su aerodromski podaci uspjeno ucitani.
	 */
	private boolean ucitajAeroPodatke(String nazivDatotekeAeroPodataka) {
		try {

			FileReader fr = new FileReader(nazivDatotekeAeroPodataka, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(fr);
			while (true) {
				String linija = br.readLine();
				if (linija == null || linija.isEmpty())
					break;
				String[] p = linija.split(";");
				Aerodrom a = new Aerodrom(p[0], p[1], p[2], p[3]);
				aeroPodaci.add(a);
			}
			System.out.println("Učitano " + aeroPodaci.size() + " meteo podataka!");
			br.close();
		} catch (IOException | NumberFormatException e) {
			if (e.getMessage().contains("Permission denied"))
				System.out.println("ERROR 29 Nije omoguceno citanje datoteke u pravima pristupa!");
			else {
				System.out.println("ERROR 29 Datoteka ne postoji!");
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	/**
	 * Obrada zahtjeva.
	 */
	public void obradaZahtjeva() {
		try (ServerSocket ss = new ServerSocket(this.port, this.maksCekaca))
		{
			while (true) {
				this.veza = ss.accept();

				this.veza.setSoTimeout(maksCekanje);
				DretvaObrade dretvaObrade = new DretvaObrade(veza);
				dretvaObrade.start();
			}

		} catch (IOException ex) {
			Logger.getLogger(ServerAerodroma.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * Obradi naredbu je metoda koja se koristi za oredivanje 
	 * koja ce se metoda dalje koristiti u izvrsavanju programa.
	 *
	 * @param zahtjev 	zahtjev.
	 * @return 			string.
	 */
	private String obradiNaredbu(String zahtjev) {
		Pattern pAero = Pattern.compile("^AIRPORT$");
		Pattern pAeroIcao = Pattern.compile("^AIRPORT ([A-Z]{4})$");
		Pattern pAeroIcaoBroj = Pattern.compile("^AIRPORT ([A-Z]{4}) (\\d{1,7})$");

		Matcher mAero = pAero.matcher(zahtjev);
		Matcher mAeroIcao = pAeroIcao.matcher(zahtjev);
		Matcher mAeroIcaoBroj = pAeroIcaoBroj.matcher(zahtjev);

		String odgovor = "ERROR 20 Format komande nije ispravan";

		if (mAero.matches()) {
			odgovor = izvrsiNaredbuAero(zahtjev);
		} else if (mAeroIcao.matches()) {
			odgovor = izvrsiNaredbuAeroIcao(zahtjev);
		} else if (mAeroIcaoBroj.matches()) {
			odgovor = izvrsiNaredbuAeroIcaoBroj(zahtjev);
		}

		return odgovor;
	}

	/**
	 * Izvrsi naredbu aero je metoda koja vraca sve icao za postojece {@link Aerodrom}.
	 *
	 * @param zahtjev 	zahtjev.
	 * @return 			string.
	 */
	private String izvrsiNaredbuAero(String zahtjev) {
		String popisRezultata = "";
		if (meduspremnik.containsKey(zahtjev)) {
			popisRezultata = meduspremnik.get(zahtjev);
		} else {
			for (Aerodrom a : aeroPodaci) {
				if (popisRezultata.length() <= 0) {
					popisRezultata = "OK " + a.icao + ";";
				} else {
					popisRezultata = popisRezultata.concat(" " + a.icao + ";");
				}

			}
		}
		if (popisRezultata.length() <= 0)
			popisRezultata = "ERROR 29 Nije pronaden niti jedan aerodrom!";
		else
			meduspremnik.putIfAbsent(zahtjev, popisRezultata);

		return popisRezultata;
	}

	/**
	 * Izvrsi naredbu aero icao je metoda koja vraca sve informacije o {@link Aerodrom}.
	 * Informacije koje se vracaju su icao, naziv, broj geografske sirine, broj geografskaduljina
	 *
	 * @param zahtjev 	zahtjev.
	 * @return 			string.
	 */
	private String izvrsiNaredbuAeroIcao(String zahtjev) {
		String[] podaci = zahtjev.split(" ");
		String icao = podaci[1];
		String popisRezultata = "";
		if (meduspremnik.containsKey(zahtjev))
			popisRezultata = meduspremnik.get(zahtjev);
		else {
			for (Aerodrom a : aeroPodaci) {

				if (a.icao.equals(icao)) {
					if (popisRezultata.length() <= 0) {
						popisRezultata = "OK " + a.icao + " \"" + a.naziv + "\" " + a.gpsGS + " " + a.gpsGD + ";";
					} else {
						popisRezultata = popisRezultata
								.concat(" " + a.icao + " \"" + a.naziv + "\" " + a.gpsGS + " " + a.gpsGD + ";");
					}
				}
			}
		}
		if (popisRezultata.length() <= 0)
			popisRezultata = "ERROR 21 Aerodrom '" + icao + "' ne postoji!";
		else
			meduspremnik.putIfAbsent(zahtjev, popisRezultata);

		return popisRezultata;
	}

	/**
	 * Izvrsi naredbu aero icao broj je metoda koja komunicira sa {@link ServerUdaljenosti} 
	 * kako bi dobila udaljenosti aerodroma te ispisala one koji zadovoljavaju uvjet.
	 *
	 * @param zahtjev 	zahtjev.
	 * @return  		string.
	 */
	private String izvrsiNaredbuAeroIcaoBroj(String zahtjev) {
		String[] podaci = zahtjev.split(" ");
		String icao = podaci[1];
		Double brojKm = Double.parseDouble(podaci[2]);
		String popisRezultata = "OK";
		if (meduspremnik.containsKey(zahtjev))
			popisRezultata = meduspremnik.get(zahtjev);
		else {
			for (Aerodrom a : aeroPodaci) {
				if (a.icao.equals(icao)) {
					for (Aerodrom a2 : aeroPodaci) {
						if (!a2.equals(a)) {
							String komanda = "DISTANCE " + a.icao + " " + a2.icao;
							String odgovor = posaljiKomandu(serverUdaljenostiAdresa, serverUdaljenostiPort, komanda);
							if(odgovor.contains("ERROR 22"))
								return odgovor;
							String[] dijeloviOdgovora = odgovor.split(" ");
							if (dijeloviOdgovora[0].contentEquals("OK")) {
								if (Double.parseDouble(dijeloviOdgovora[1]) <= brojKm) {
									popisRezultata = popisRezultata
											.concat(" " + a2.icao+" " + dijeloviOdgovora[1] + ";");
								}
							}
						}
					}

				}
			}
		}
		meduspremnik.putIfAbsent(zahtjev, popisRezultata);

		return popisRezultata;
	}

	/**
	 * Konfiguracija sadrzi je metoda koja provjerava sadri li konfiguracija odredeni parametar.
	 *
	 * @param kljuc 	je ime parametra.
	 * @return true, 	ako konfiguracija sadrzi parametar.
	 */
	private static boolean konfiguracijaSadrzi(String kljuc) {
		if (konfig.dajPostavku(kljuc) == null || konfig.dajPostavku(kljuc).isEmpty()) {
			System.out.println("ERROR 29 " + kljuc + " nije definiran u konfiguraciji!");
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
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("ERROR 29 Port se vec koristi!");
			return false;
		}
		return true;
	}

	/**
	 * Klasa DretvaObrade koja se koristi za ostvarivanje visedretvenog rada.
	 */
	public class DretvaObrade extends Thread {
		
		/** Veza na mrežnu utičnicu. */
		private Socket veza = null;

		/**
		 * Konstruktor klase {@see DretvaObrade}.
		 *
		 * @param veza 	veza.
		 */
		public DretvaObrade(Socket veza) {
			super();
			this.veza = veza;
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
			// TODO Auto-generated method stub
			super.run();
			try (InputStreamReader isr = new InputStreamReader(this.veza.getInputStream(), Charset.forName("UTF-8"));
					OutputStreamWriter osw = new OutputStreamWriter(this.veza.getOutputStream(),
							Charset.forName("UTF-8"));) {
				veza.setSoTimeout(maksCekanje);

				StringBuilder tekst = new StringBuilder();
				while (true) {
					int i = isr.read();
					if (i == -1) {
						break;
					}
					tekst.append((char) i);
				}

				this.veza.shutdownInput();

				String odgovor = obradiNaredbu(tekst.toString());
				osw.write(odgovor);
				osw.flush();
				this.veza.shutdownOutput();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Prekid.
		 */
		@Override
		public void interrupt() {
			super.interrupt();
		}
	}

	/**
	 * Posalji komandu salje komandu na odredenu adresu i port.
	 *
	 * @param adresa 	adresa.
	 * @param port 		port.
	 * @param komanda 	komanda.
	 * @return 			string.
	 */
	public String posaljiKomandu(String adresa, int port, String komanda) {
		try (Socket veza = new Socket(adresa, port);
				InputStreamReader isr = new InputStreamReader(veza.getInputStream(), Charset.forName("UTF-8"));
				OutputStreamWriter osw = new OutputStreamWriter(veza.getOutputStream(), Charset.forName("UTF-8"));) {
			veza.setSoTimeout(maksCekanje);

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
			return "ERROR 22 serverUdaljenosti ne radi";
		} catch (IOException ex) {
			return "ERROR 29 vrijeme cekanja na odgovor je isteklo";
		}
	}

	/**
	 * Ispis je metoda koja ispisuje na ekran.
	 *
	 * @param message  message.
	 */
	private void ispis(String message) {
		System.out.println(message);
	}

	
}
