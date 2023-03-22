package org.foi.nwtis.bsikac.aplikacija_5.ws;

import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.bsikac.aplikacija_5.modeli.AerodromiPraceni;
import org.foi.nwtis.bsikac.aplikacija_5.slusaci.SlusacAplikacije;
import org.foi.nwtis.bsikac.aplikacija_5.transfer.AerodromiKlijent;
import org.foi.nwtis.bsikac.aplikacija_5.wsock.Info;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
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
	
	@Inject
	Info info;
	
	/**  Kontekst servisa. */
	@Resource
	private WebServiceContext wsContext;
	
	/**  Kontekst aplikacije. */
	@Inject
	ServletContext context; 
	
	

	@WebMethod
	public List<AvionLeti> dajPolaskeDan(String korisnik, String zeton, String icao, String danOd, String danDo)
	{
		return AerodromiKlijent.dajPolaske(korisnik, zeton, icao, danOd, danDo);
	}
	
	
	@WebMethod
	public List<AvionLeti> dajPolaskeVrijeme(String korisnik, String zeton, String icao, String vrijemeOd, String vrijemeDo)
	{
		return AerodromiKlijent.dajPolaske(korisnik, zeton, icao, vrijemeOd, vrijemeDo);
	}
	
	/**
	 * Dodaj aerodrom za pratiti.
	 *
	 * @param icao  icao
	 * @return  boolean
	 */
	@WebMethod
	public boolean dodajAerodromZaPratiti(String korisnik, String zeton, String icao)
	{
		boolean dodano = AerodromiKlijent.dodajAerodromZaPracenje(korisnik, zeton, icao);
		if(dodano)
		{
			info.informiraj("Dodan je aerodrom za preuzimanje");
			return true;
		}
		else
			return false;
	}

}
