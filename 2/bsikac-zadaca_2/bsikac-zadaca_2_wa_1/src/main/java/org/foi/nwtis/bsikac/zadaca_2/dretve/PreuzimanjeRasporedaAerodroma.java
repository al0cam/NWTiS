package org.foi.nwtis.bsikac.zadaca_2.dretve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.bsikac.zadaca_2.modeli.AerodromiPraceni;
import org.foi.nwtis.bsikac.zadaca_2.modeli.AerodromiProblemi;
import org.foi.nwtis.bsikac.zadaca_2.podaci.AerodromDolasciDAO;
import org.foi.nwtis.bsikac.zadaca_2.podaci.AerodromPolasciDAO;
import org.foi.nwtis.bsikac.zadaca_2.podaci.AerodromPraceniDAO;
import org.foi.nwtis.bsikac.zadaca_2.podaci.AerodromProblemDAO;
import org.foi.nwtis.bsikac.zadaca_2.slusaci.SlusacAplikacije;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;

/**
 * Klasa PreuzimanjeRasporedaAerodroma koja se koristi za dohvacanje vrijednosti letova sa openSky servisa.
 */
public class PreuzimanjeRasporedaAerodroma extends Thread {

	
	/** konfiguracija. */
private PostavkeBazaPodataka konf = null;
	
	/** Vrijednost sata u sekundama. */
	final int sat = 3600;
	
	/** Vrijednost dana u sekundama. */
	final int dan = 86400;
	
	/** Trajanje jednog ciklusa povlacenja podataka. */
	private int ciklusVrijeme;
	
	/** Broj ciklusa u kojem se obavlja korekcija. */
	private int ciklusKorekcija;
	
	/** Odmak od trenutnog vremena do kojeg se podaci preuzimaju. */
	private int preuzimanjeOdmak;
	
	/** Pauza nakon svakog zahtjeva. */
	private int preuzimanjePauza;
	
	/** Datum od kojeg zapocinje preuzimanje. */
	private int preuzimanjeOd;
	
	/** Datum do kojeg traje preuzimanje. */
	private int preuzimanjeDo;
	
	/** Vrijeme u satima za koje se preuzimaju podaci u jednom ciklusu. */
	private int preuzimanjeVrijeme;
	
	/** Vrijeme koje se prati prilikom izvrsenja ciklusa. */
	private int vrijemeObrade;
	
	/** Trenutni datum. */
	private int trenutniDatum;
	
	/** Preuzimanje do korekcija. */
	private int preuzimanjeDoKorekcija;
	
	/** Korisnicko ime za opensky. */
	private String korime;
	
	/** Lozinka za opensky. */
	private String lozinka;
	
	/** os klijent. */
	private OSKlijent osKlijent;

	/**
	 * Start.
	 */
	@Override
	public synchronized void start() {
		konf = SlusacAplikacije.getDbKonf();
		
		
		this.ciklusVrijeme = Integer.parseInt(konf.dajPostavku("ciklus.vrijeme"));
		this.ciklusKorekcija = Integer.parseInt(konf.dajPostavku("ciklus.korekcija"));
		this.preuzimanjeOdmak = Integer.parseInt(konf.dajPostavku("preuzimanje.odmak"));
		this.preuzimanjePauza = Integer.parseInt(konf.dajPostavku("preuzimanje.pauza"));
		try {
			this.preuzimanjeOd = pretvoriDatumUSekunde(konf.dajPostavku("preuzimanje.od"));
		} catch (ParseException e) {
		}
		try {
			this.preuzimanjeDo = (int) pretvoriDatumUSekunde(konf.dajPostavku("preuzimanje.do"));
		} catch (ParseException e) {
		}
		this.preuzimanjeVrijeme = Integer.parseInt(konf.dajPostavku("preuzimanje.vrijeme"));
		this.vrijemeObrade = preuzimanjeOd;
		
		this.korime = konf.dajPostavku("OpenSkyNetwork.lozinka");
		this.lozinka = konf.dajPostavku("OpenSkyNetwork.korisnik");

		this.osKlijent = new OSKlijent(korime, lozinka);

		super.start();
	}

	/**
	 * Run.
	 */
	@Override
	public void run() {
		AerodromPolasciDAO.setDbKonf(konf);
		AerodromDolasciDAO.setDbKonf(konf);
		AerodromPraceniDAO.setDbKonf(konf);
		AerodromProblemDAO.setDbKonf(konf);
		
		AerodromiPraceni[] aerodromi = AerodromPraceniDAO.dohvatiSveAerodromePracene();

		this.trenutniDatum = (int) (System.currentTimeMillis()/1000);
		this.preuzimanjeDoKorekcija = (int) (trenutniDatum - this.dan*this.preuzimanjeOdmak);
		
		int vrijemeDohvata;
		int vrijemeEfektivnogRada = 0;
		int brojCiklusa = 0;
		int brojVirtualnogCiklusa = 0;
		int pocetakEfektivnogRada;
		while (this.vrijemeObrade < preuzimanjeDoKorekcija) {
			brojCiklusa++;
			pocetakEfektivnogRada = (int) ZonedDateTime.now().toInstant().toEpochMilli();
			
			vrijemeDohvata = this.vrijemeObrade + preuzimanjeVrijeme*sat;
			
			for (AerodromiPraceni a : aerodromi) {

				List<AvionLeti> avioniPolasci;
				try {
					avioniPolasci = osKlijent.getDepartures(a.getIdent(), this.preuzimanjeOd, vrijemeDohvata);
					if (avioniPolasci != null) {
						System.out.println("Broj letova: " + avioniPolasci.size());
						for (AvionLeti avion : avioniPolasci) {
							
							if(avion.getEstArrivalAirport()!= null)
								AerodromPolasciDAO.dodajAerodromPolazak(avion);
							System.out.println(
									"Avion: " + avion.getIcao24() + " Odredište: " + avion.getEstArrivalAirport());
						}
					}
				} catch (NwtisRestIznimka e) {
					AerodromiProblemi p = new AerodromiProblemi();
					p.setIdent(a.getIdent());
					p.setDescription(e.getMessage());
					AerodromProblemDAO.dodajAerodromProblem(p);
				}

				System.out.println("Dolasci na aerodrom: " + a.getIdent());
				List<AvionLeti> avioniDolasci;
				try {
					avioniDolasci = osKlijent.getArrivals(a.getIdent(), this.preuzimanjeOd, vrijemeDohvata);
					if (avioniDolasci != null) {
						System.out.println("Broj letova: " + avioniDolasci.size());
						for (AvionLeti avion : avioniDolasci) {
							if(avion.getEstDepartureAirport()!= null)
								AerodromDolasciDAO.dodajAerodromDolazak(avion);
							System.out.println(
									"Avion: " + avion.getIcao24() + " Odredište: " + avion.getEstDepartureAirport());
						}
					}
				} catch (NwtisRestIznimka e) {
					AerodromiProblemi p = new AerodromiProblemi();
					p.setIdent(a.getIdent());
					p.setDescription(e.getMessage());
					AerodromProblemDAO.dodajAerodromProblem(p);
				}
				try {
					sleep(preuzimanjePauza);
				} catch (InterruptedException e) {
				}
			}
			vrijemeEfektivnogRada = (int) ZonedDateTime.now().toInstant().toEpochMilli() - pocetakEfektivnogRada;

			int vrijemeSpavanja = 0;
			if(brojCiklusa!= 0 && brojCiklusa %  ciklusKorekcija == 0)
			{
				int broj = pronadjiBrojCiklusa(vrijemeEfektivnogRada);
				brojVirtualnogCiklusa+=broj;
				vrijemeSpavanja = brojVirtualnogCiklusa*ciklusVrijeme - vrijemeEfektivnogRada;
			}
			else
			{
				int broj = pronadjiBrojCiklusa(vrijemeEfektivnogRada);
				brojVirtualnogCiklusa+=broj;
				vrijemeSpavanja = pronadjiBrojCiklusa(vrijemeEfektivnogRada)*ciklusVrijeme-vrijemeEfektivnogRada;
			}
			try {
				sleep(vrijemeSpavanja);
			} catch (InterruptedException e) {
			}
			this.vrijemeObrade = vrijemeDohvata;
		}
	}

	/**
	 * Interrupt.
	 */
	@Override
	public void interrupt() {
		super.interrupt();
	}
	
	/**
	 * Pronadji broj ciklusa.
	 *
	 * @param efektivnoVrijeme 	efektivno vrijeme koje je dretva provela u povlacenju podataka
	 * @return int
	 */
	public int pronadjiBrojCiklusa(int efektivnoVrijeme)
	{
		int i;
		for(i=1;efektivnoVrijeme>(this.ciklusVrijeme*i); i++);
		return i;
	}
	
	/**
	 * Pretvori datum U sekunde.
	 *
	 * @param datum datum
	 * @return the int
	 * @throws ParseException the parse exception
	 */
	public static int pretvoriDatumUSekunde(String datum) throws ParseException {
		String formatBezSati = "[0-3]\\d\\.[0-1]\\d\\.\\d{4}";
		String formatSaSatima = "[0-3]\\d\\.[0-1]\\d\\.\\d{4} [0-2]\\d:[0-6]\\d:[0-6]\\d";
		SimpleDateFormat sdf = null;
		if(datum.matches(formatBezSati))
			sdf = new SimpleDateFormat("dd.MM.yyyy");
		else if(datum.matches(formatSaSatima))
			sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
       
        Date date = sdf.parse(datum);
        return (int) (date.getTime()/1000);
    }

}
