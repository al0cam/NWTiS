package org.foi.nwtis.bsikac.aplikacija_2.dretve;

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

import org.foi.nwtis.bsikac.aplikacija_2.modeli.AerodromiPraceni;
import org.foi.nwtis.bsikac.aplikacija_2.modeli.AerodromiProblemi;
import org.foi.nwtis.bsikac.aplikacija_2.podaci.AerodromDolasciDAO;
import org.foi.nwtis.bsikac.aplikacija_2.podaci.AerodromPolasciDAO;
import org.foi.nwtis.bsikac.aplikacija_2.podaci.AerodromPraceniDAO;
import org.foi.nwtis.bsikac.aplikacija_2.podaci.AerodromProblemDAO;
import org.foi.nwtis.bsikac.aplikacija_2.slusaci.SlusacAplikacije;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
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
	
	/** Vrijednost sata u milisekundama. */
	final int sat = 3600*1000;
	
	/** Vrijednost dana u milisekundama. */
	final int dan = 86400*1000;
	
	/** Trajanje jednog ciklusa povlacenja podataka. */
	private int ciklusVrijeme; // dtc 
	
	/** Broj ciklusa u kojem se obavlja korekcija. */
	private int ciklusKorekcija; // ck
	
	/** Odmak od trenutnog vremena do kojeg se podaci preuzimaju. */
	private int preuzimanjeOdmak;
	
	/** Pauza nakon svakog zahtjeva. */
	private int preuzimanjePauza;
	
	/** Datum od kojeg zapocinje preuzimanje. */
	private long preuzimanjeOd;
	
	/** Datum do kojeg traje preuzimanje. */
	private long preuzimanjeDo;
	
	/** Vrijeme u satima za koje se preuzimaju podaci u jednom ciklusu. */
	private int preuzimanjeVrijeme;
	
	/** Vrijeme koje se prati prilikom izvrsenja ciklusa. */
	private long vrijemeObrade;
	
	/** Trenutni datum u milisekundama. */
	private long trenutniDatum;
	
	/** Preuzimanje do korekcija. */
	private long preuzimanjeDoKorekcija;
	
	/** Korisnicko ime za opensky. */
	private String korime;
	
	/** Lozinka za opensky. */
	private String lozinka;
	
	/** os klijent. */
	private OSKlijent osKlijent;
	
	private static boolean prekidac = false;
	

	/**
	 * Start.
	 */
	@Override
	public synchronized void start() {
		konf = SlusacAplikacije.getDbKonf();
		
		this.ciklusVrijeme = Integer.parseInt(konf.dajPostavku("ciklus.vrijeme"))*1000;
		this.ciklusKorekcija = Integer.parseInt(konf.dajPostavku("ciklus.korekcija"));
		this.preuzimanjeOdmak = Integer.parseInt(konf.dajPostavku("preuzimanje.odmak"));
		this.preuzimanjePauza = Integer.parseInt(konf.dajPostavku("preuzimanje.pauza"));
		this.preuzimanjeOd = pretvoriDatumUMilisekunde(konf.dajPostavku("preuzimanje.od"));
		this.preuzimanjeDo = pretvoriDatumUMilisekunde(konf.dajPostavku("preuzimanje.do"));
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
		
		this.trenutniDatum = System.currentTimeMillis();
		this.preuzimanjeDoKorekcija = trenutniDatum - this.dan*this.preuzimanjeOdmak;
		
		long vrijemeDohvata = 0;
		long vrijemeEfektivnogRada = 0;
		int brojCiklusa = 0; // j
		int brojVirtualnogCiklusa = 0; // k
		long pocetakEfektivnogRada;
		int vrijemeRadaISpavanja = 0;
		long vrijemeSpavanja = 0;
		int broj = 0;

		List<AvionLeti> avioniPolasci;
		List<AvionLeti> avioniDolasci;
		
//		System.out.println("PREUZIMANJE DO: "+preuzimanjeDo);
		while (this.vrijemeObrade < this.preuzimanjeDo && !prekidac) {
			brojCiklusa++;
			pocetakEfektivnogRada =  ZonedDateTime.now().toInstant().toEpochMilli();

			System.out.println("VRIJEME DOHVATA PRIJE: "+vrijemeDohvata);
			vrijemeDohvata = this.vrijemeObrade + preuzimanjeVrijeme*sat;
			System.out.println("VRIJEME DOHVATA POSLJE: "+vrijemeDohvata);
			if(vrijemeDohvata > preuzimanjeDoKorekcija) 
			{
				vrijemeSpavanja = this.dan;
				brojVirtualnogCiklusa += this.dan/this.ciklusVrijeme;
				preuzimanjeDoKorekcija += this.dan;
			}
			else 
			{
				for (AerodromiPraceni a : aerodromi) {
					
					try {
						System.out.println("VRIJEME OBRADE: "+this.vrijemeObrade);
						System.out.println("VRIJEME DOHVATA: "+vrijemeDohvata);
						avioniPolasci = osKlijent.getDepartures(a.getIdent(), this.vrijemeObrade/1000, vrijemeDohvata/1000);
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
	
					System.out.println("DOLASCI NA AERODROM: " + a.getIdent());
					try {
						avioniDolasci = osKlijent.getArrivals(a.getIdent(), this.vrijemeObrade/1000, vrijemeDohvata/1000);
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
				vrijemeEfektivnogRada = ZonedDateTime.now().toInstant().toEpochMilli() - pocetakEfektivnogRada;
				vrijemeRadaISpavanja += vrijemeEfektivnogRada;
				broj = pronadjiBrojCiklusa(vrijemeEfektivnogRada);
				
				brojVirtualnogCiklusa += broj;
				
				if(brojCiklusa != 0 && brojCiklusa %  ciklusKorekcija == 0)
				{
					vrijemeSpavanja = brojVirtualnogCiklusa*ciklusVrijeme - vrijemeRadaISpavanja;
				}
				else
				{
					vrijemeSpavanja = broj*ciklusVrijeme - vrijemeEfektivnogRada;
				}
			}

			if(prekidac)
				break;
			
			
			vrijemeRadaISpavanja += vrijemeSpavanja;
//			System.out.println("VRIJEME UK RADA I SPAVANJA: "+vrijemeRadaISpavanja);
//			System.out.println("BROJ: "+broj);
//			System.out.println("cik:"+ brojCiklusa);
//			System.out.println("virt:"+ brojVirtualnogCiklusa);
//			System.out.println("EFEKTIVNO: "+vrijemeEfektivnogRada);
//			System.out.println("VRIJEME SPAVANJA: "+vrijemeSpavanja);
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
		prekidac = true;
		super.interrupt();
	}
	
	/**
	 * Pronadji broj ciklusa.
	 *
	 * @param efektivnoVrijeme 	efektivno vrijeme koje je dretva provela u povlacenju podataka
	 * @return int
	 */
	public int pronadjiBrojCiklusa(long efektivnoVrijeme)
	{
		int i;
		for(i=1;efektivnoVrijeme>(this.ciklusVrijeme*i); i++);
		return i;
	}
	
	/**
	 * Pretvori datum U milisekunde.
	 *
	 * @param datum datum
	 * @return the int
	 * @throws ParseException the parse exception
	 */
	public static long pretvoriDatumUMilisekunde(String datum) {
		System.out.println(datum);
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
			return date.getTime();
		} catch (ParseException e) {
			System.out.println("Datum je krivo unesen");
		}
        return -1;
    }

}
