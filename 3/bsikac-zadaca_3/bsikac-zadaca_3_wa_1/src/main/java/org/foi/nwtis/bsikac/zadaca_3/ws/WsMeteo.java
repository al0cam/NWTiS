package org.foi.nwtis.bsikac.zadaca_3.ws;

import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.bsikac.zadaca_3.podaci.AerodromDAO;
import org.foi.nwtis.bsikac.zadaca_3.slusaci.SlusacAplikacije;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.servlet.ServletContext;
import jakarta.xml.ws.WebServiceContext;

/**
 * Klasas WsMeteo.
 */
@WebService(serviceName = "meteo")
public class WsMeteo {
	
	/** Kontekst. */
	@Resource
	private WebServiceContext wsContext;
	
	/** Postavke baze podataka. */
	PostavkeBazaPodataka pbp;
	
	/**
	 * Daj meteo.
	 *
	 * @param icao  icao
	 * @return  meteo podaci
	 */
	@WebMethod
	public MeteoPodaci dajMeteo(String icao){
        pbp = SlusacAplikacije.getDbKonf();
		AerodromDAO.setDbKonf(pbp);
		Aerodrom[] aerodromi = AerodromDAO.dohvatiSveAerodrome();
        
        Aerodrom trazeni = null;
        for(Aerodrom a : aerodromi) {
        	if(a.getIcao().compareTo(icao) == 0)
        		trazeni =a;
        }
        
        Lokacija l = trazeni.getLokacija();
        
        
        OWMKlijent owmKlijent = new OWMKlijent(pbp.dajPostavku("OpenWeatherMap.apikey"));
        MeteoPodaci meteoPodaci = null;
        try {
			meteoPodaci = owmKlijent.getRealTimeWeather(l.getLatitude(), l.getLatitude());
		} catch (NwtisRestIznimka e) {
			e.printStackTrace();
		}
        
        return meteoPodaci;
	}
	
	
	/*
	 * ServletContext context = (ServletContext)
wsContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
	 * */
}
