package org.foi.nwtis.bsikac.zadaca_3.ws;

import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.bsikac.zadaca_3.modeli.AerodromiPraceni;
import org.foi.nwtis.bsikac.zadaca_3.podaci.AerodromDAO;
import org.foi.nwtis.bsikac.zadaca_3.podaci.AerodromDolasciDAO;
import org.foi.nwtis.bsikac.zadaca_3.podaci.AerodromPolasciDAO;
import org.foi.nwtis.bsikac.zadaca_3.podaci.AerodromPraceniDAO;
import org.foi.nwtis.bsikac.zadaca_3.slusaci.SlusacAplikacije;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.servlet.ServletContext;
import jakarta.xml.ws.WebServiceContext;

/**
 * Klasa WsAerodromi.
 */
@WebService(serviceName = "aerodromi")
public class WsAerodromi {
	
	/**  Kontekst servisa. */
	@Resource
	private WebServiceContext wsContext;
	
	/**  Kontekst aplikacije. */
	@Inject
	ServletContext context; 
	
	/**  Postavke baze podataka. */
	PostavkeBazaPodataka pbp = SlusacAplikacije.getDbKonf();
	
	/**
	 * Daj sve aerodrome.
	 *
	 * @return  aerodrom[]
	 */
	@WebMethod
	public Aerodrom[] dajSveAerodrome(){
		pbp = SlusacAplikacije.getDbKonf();
		AerodromDAO.setDbKonf(pbp);
		Aerodrom[] aerodromi = AerodromDAO.dohvatiSveAerodrome();
        
        return aerodromi;
	}
	
	/**
	 * Daj aerodrome za pratiti.
	 *
	 * @return  aerodromi praceni[]
	 */
	@WebMethod
	public AerodromiPraceni[] dajAerodromeZaPratiti()
	{
		AerodromPraceniDAO.setDbKonf(pbp);
		AerodromiPraceni[] ai = AerodromPraceniDAO.dohvatiSveAerodromePracene();
		for(AerodromiPraceni a: ai)
		 System.out.println(a.toString());
		return ai;
		
	}
	
	/**
	 * Daj polaske aerodroma.
	 *
	 * @param icao  icao
	 * @param datum  datum
	 * @return  avion leti[]
	 */
	@WebMethod
	public AvionLeti[] dajPolaskeAerodroma(String icao, String datum)
	{
		AerodromPolasciDAO.setDbKonf(pbp);
		AvionLeti[] ai = AerodromPolasciDAO.dohvatiAerodromPolaskZaIcaoNaDan(icao,datum);
		return ai;
	}
	
	/**
	 * Daj dolaske aerodroma.
	 *
	 * @param icao  icao
	 * @param datum  datum
	 * @return  avion leti[]
	 */
	@WebMethod
	public AvionLeti[] dajDolaskeAerodroma(String icao, String datum)
	{
		AerodromDolasciDAO.setDbKonf(pbp);
		AvionLeti[] ai = AerodromDolasciDAO.dohvatiAerodromDolaskZaIcaoNaDan(icao,datum);
		return ai;
	}
	
	/**
	 * Dodaj aerodrom za pratiti.
	 *
	 * @param icao  icao
	 * @return  boolean
	 */
	@WebMethod
	public Boolean dodajAerodromZaPratiti(String icao)
	{
		AerodromPraceniDAO.setDbKonf(pbp);
		AerodromiPraceni ap = new AerodromiPraceni();
		ap.setIdent(icao);
		return AerodromPraceniDAO.dodajAerodromPraceni(ap);
	}
	
	/**
	 * Daj najblizi aerodrom.
	 *
	 * @param lokacija  lokacija
	 * @param vrsta  vrsta
	 * @return  aerodrom
	 */
	@WebMethod
	public Aerodrom dajNajbliziAerodrom(Lokacija lokacija, Boolean vrsta)
	{
		AerodromPraceniDAO.setDbKonf(pbp);
		AerodromDAO.setDbKonf(pbp);
		Aerodrom najblizi = null;
		Aerodrom[] aerodromi = null;
		if(vrsta == true)
		{
			AerodromiPraceni[] ai = AerodromPraceniDAO.dohvatiSveAerodromePracene();
			ArrayList<Aerodrom> listaAerodroma = new ArrayList<>();
			for(AerodromiPraceni ap: ai)
			{
				listaAerodroma.add(AerodromDAO.dohvatiAerodrom(ap.getIdent()));
			}
			aerodromi = new Aerodrom[listaAerodroma.size()];
	        int i = 0;
	        for (Aerodrom korisnik : listaAerodroma) {
	        	aerodromi[i++] = korisnik;
	        }
		}
		else
		{
			aerodromi = AerodromDAO.dohvatiSveAerodrome();
		}
		
		if(aerodromi.length > 0)
		{
			najblizi = aerodromi[0];
			double najmanjaUdaljenost = udaljenostDvijeTockeNaSferi(lokacija, najblizi);
			double novaUdaljenost = 0;
			for(Aerodrom a: aerodromi)
			{
				novaUdaljenost = udaljenostDvijeTockeNaSferi(lokacija, a);
				if(novaUdaljenost< najmanjaUdaljenost) {
					najblizi = a;
					najmanjaUdaljenost = novaUdaljenost;
				}
			}
		}
		
		return najblizi;
	}
	
	
	/**
	 * Udaljenost dvije tocke na sferi.
	 *
	 * @param lokacija  lokacija
	 * @param icao2  icao 2
	 * @return  double
	 */
	static double udaljenostDvijeTockeNaSferi(Lokacija lokacija, Aerodrom icao2) {

		
		
		double dLat = Math.toRadians(
				Double.parseDouble(icao2.getLokacija().getLatitude()) - Double.parseDouble(lokacija.getLatitude()));
		double dLon = Math.toRadians(
				Double.parseDouble(icao2.getLokacija().getLongitude()) - Double.parseDouble(lokacija.getLongitude()));

		double gs1 = Math.toRadians(Double.parseDouble(lokacija.getLatitude()));
		double gs2 = Math.toRadians(Double.parseDouble(icao2.getLokacija().getLatitude()));

		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(gs1) * Math.cos(gs2);
		double radiusZemlje = 6371;
		double c = 2 * Math.asin(Math.sqrt(a));
		return zaokruzi(radiusZemlje * c);
	}
	
	/**
	 * Zaokruzi.
	 *
	 * @param broj  broj
	 * @return  double
	 */
	private static double zaokruzi(double broj) {
		return Math.round(broj);
	}
	
	/*
	 * ServletContext context = (ServletContext)
wsContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
	 * */
}
