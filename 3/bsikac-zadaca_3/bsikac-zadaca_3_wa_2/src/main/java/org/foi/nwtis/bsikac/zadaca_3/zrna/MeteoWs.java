package org.foi.nwtis.bsikac.zadaca_3.zrna;


import org.foi.nwtis.bsikac.ws.meteo.Meteo;
import org.foi.nwtis.bsikac.ws.meteo.MeteoPodaci;
import org.foi.nwtis.bsikac.ws.meteo.WsMeteo;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.xml.ws.WebServiceRef;

/**
 * Klasa MeteoWs.
 */
@RequestScoped
@Named("meteoWs")
public class MeteoWs {

	/**  service. */
	@WebServiceRef(wsdlLocation = "http://localhost:9090/bsikac-zadaca_3_wa_1/meteo?wsdl")
	private Meteo service;
	
	/**  icao. */
	private String icao = "LDZA";
	
	/**  mp. */
	private MeteoPodaci mp = new MeteoPodaci();
	
	/**
	 * Daj meteo podatke.
	 *
	 * @param icao  icao
	 * @return  meteo podaci
	 */
	public MeteoPodaci dajMeteoPodatke(String icao) {
		if(service==null) service = new Meteo();
		WsMeteo wsMeteo = service.getWsMeteoPort();
		MeteoPodaci mp = wsMeteo.dajMeteo(icao);
		return mp;
	}

	/**
	 * Dohvaca  icao.
	 *
	 * @return  icao
	 */
	public String getIcao() {
		return icao;
	}

	/**
	 * Postavlja  icao.
	 *
	 * @param icao 	icao
	 */
	public void setIcao(String icao) {
		this.icao = icao;
	}

	/**
	 * Dohvaca  mp.
	 *
	 * @return  mp
	 */
	public MeteoPodaci getMp() {
		this.mp= dajMeteoPodatke(icao);
		return mp;
	}

	/**
	 * Postavlja  mp.
	 *
	 * @param mp  mp
	 */
	public void setMp(MeteoPodaci mp) {
		this.mp = mp;
	}

}
