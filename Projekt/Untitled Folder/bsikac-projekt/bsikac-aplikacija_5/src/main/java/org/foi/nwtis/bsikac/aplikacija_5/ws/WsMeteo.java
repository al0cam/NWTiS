package org.foi.nwtis.bsikac.aplikacija_5.ws;

import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.bsikac.aplikacija_5.modeli.MeteoPodaciJMS;
import org.foi.nwtis.bsikac.aplikacija_5.slusaci.SlusacAplikacije;
import org.foi.nwtis.bsikac.aplikacija_5.transfer.AerodromiKlijent;
import org.foi.nwtis.bsikac.aplikacija_5.transfer.PorukeKlijent;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

import com.google.gson.Gson;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConnectionFactoryDefinition;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.Session;
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
	
	private static PostavkeBazaPodataka pbp = null;


	/**
	 * Daj meteo.
	 *
	 * @param icao icao
	 * @return meteo podaci
	 */
	@WebMethod
	public MeteoPodaci dajMeteo(String korisnik, String zeton, String icao) {

		postaviDbKonf();
		Aerodrom trazeni = AerodromiKlijent.dohvatiAerodrom(korisnik, zeton, icao);

		Lokacija l = trazeni.getLokacija();

		OWMKlijent owmKlijent = new OWMKlijent(pbp.dajPostavku("OpenWeatherMap.apikey"));
		MeteoPodaci meteoPodaci = null;
		try {
			meteoPodaci = owmKlijent.getRealTimeWeather(l.getLatitude(), l.getLongitude());
		} catch (NwtisRestIznimka e) {
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		String meteoPodaciJson = gson.toJson(meteoPodaci,MeteoPodaci.class);
		MeteoPodaciJMS m = gson.fromJson(meteoPodaciJson, MeteoPodaciJMS.class);
		PorukeKlijent.posaljiJMSPoruku(m);

		return meteoPodaci;
	}

	public static void postaviDbKonf() {
		if (pbp == null) {
			pbp = SlusacAplikacije.getDbKonf();
		}
	}


}
