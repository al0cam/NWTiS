package org.foi.nwtis.bsikac.zadaca_3.zrna;

import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.bsikac.ws.aerodromi.Aerodrom;
import org.foi.nwtis.bsikac.ws.aerodromi.Aerodromi;
import org.foi.nwtis.bsikac.ws.aerodromi.AerodromiPraceni;
import org.foi.nwtis.bsikac.ws.aerodromi.AvionLeti;
import org.foi.nwtis.bsikac.ws.aerodromi.WsAerodromi;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.xml.ws.WebServiceRef;

/**
 * Klasa AerodromiWs.
 */
@RequestScoped
@Named("aerodromiWs")
public class AerodromiWs {
	
	/**  service. */
	@WebServiceRef(wsdlLocation = "http://localhost:9090/bsikac-zadaca_3_wa_1/aerodromi?wsdl")
	private Aerodromi service;
	
	/**  aerodromi. */
	private List<Aerodrom> aerodromi = new ArrayList<Aerodrom>();
	
	/**  aerodromi praceni. */
	private List<AerodromiPraceni> aerodromiPraceni = new ArrayList<AerodromiPraceni>();
	
	/**  polasci. */
	private List<AvionLeti> polasci = new ArrayList<AvionLeti>();
	
	/**  dolasci. */
	private List<AvionLeti> dolasci = new ArrayList<AvionLeti>();
	
	/**  dolasci polasci. */
	private List<AvionLeti> dolasciPolasci = new ArrayList<AvionLeti>();
	
	/**  icao. */
	private String icao = "LDZA";
	
	/**  datum. */
	private String datum = "01.01.2022";
	
	/**  mw. */
	private MeteoWs mw = new MeteoWs();
	
	
	
	
	/**
	 * Daj sve aerodrome.
	 *
	 * @return  list
	 */
	public List<Aerodrom> dajSveAerodrome(){
		if(service == null) service = new Aerodromi();
		WsAerodromi wsAerdoromi = service.getWsAerodromiPort();
		List<Aerodrom> aerodromi = wsAerdoromi.dajSveAerodrome();
		return aerodromi;
	}
	
	/**
	 * Daj pracene aerodrome.
	 *
	 * @return  list
	 */
	public List<AerodromiPraceni> dajPraceneAerodrome(){
		if(service == null) service = new Aerodromi();
		WsAerodromi wsAerdoromi = service.getWsAerodromiPort();
		List<AerodromiPraceni> aerodromi = wsAerdoromi.dajAerodromeZaPratiti();
		return aerodromi;
	}
	
	/**
	 * Dodaj praceni aerodrom.
	 *
	 * @param icao  icao
	 * @return  boolean
	 */
	public Boolean dodajPraceniAerodrom(String icao){
		if(service == null) service = new Aerodromi();
		WsAerodromi wsAerdoromi = service.getWsAerodromiPort();
		Boolean aerodromi = wsAerdoromi.dodajAerodromZaPratiti(icao);
		System.out.println("BRUV: "+aerodromi);
		return aerodromi;
	}
	
	/**
	 * Daj polaske.
	 *
	 * @param icao  icao
	 * @param datum  datum
	 * @return  list
	 */
	public List<AvionLeti> dajPolaske(String icao, String datum){
		if(service == null) service = new Aerodromi();
		WsAerodromi wsAerdoromi = service.getWsAerodromiPort();
		List<AvionLeti> aerodromi = wsAerdoromi.dajPolaskeAerodroma(icao, datum);
		return aerodromi;
	}
	
	/**
	 * Daj dolaske.
	 *
	 * @param icao  icao
	 * @param datum  datum
	 * @return  list
	 */
	public List<AvionLeti> dajDolaske(String icao, String datum){
		if(service == null) service = new Aerodromi();
		WsAerodromi wsAerdoromi = service.getWsAerodromiPort();
		List<AvionLeti> aerodromi = wsAerdoromi.dajDolaskeAerodroma(icao, datum);
		return aerodromi;
	}
	
	/**
	 * Daj dolaske polaske.
	 *
	 * @param icao  icao
	 * @param datum  datum
	 * @return  list
	 */
	public List<AvionLeti> dajDolaskePolaske(String icao, String datum){
		List<AvionLeti> aerodromi= dajDolaske(icao, datum);
		aerodromi.addAll(dajPolaske(icao, datum));
		return aerodromi;
	}
	
	/**
	 * Daj najblizi aerodrom.
	 *
	 * @param icao  icao
	 * @param datum  datum
	 * @return  list
	 */
	public List<AvionLeti> dajNajbliziAerodrom(String icao, String datum){
//		TODO: napisat ovo
		return null;
	}
	

	/**
	 * Dohvaca  aerodromi.
	 *
	 * @return  aerodromi
	 */
	public List<Aerodrom> getAerodromi() {
		this.aerodromi = dajSveAerodrome();
		return aerodromi;
	}

	/**
	 * Postavlja  aerodromi.
	 *
	 * @param aerodromi  new aerodromi
	 */
	public void setAerodromi(List<Aerodrom> aerodromi) {
		this.aerodromi = aerodromi;
	}
	
	/**
	 * Dohvaca  aerodromi praceni.
	 *
	 * @return  aerodromi praceni
	 */
	public List<AerodromiPraceni> getAerodromiPraceni() {
		this.aerodromiPraceni = dajPraceneAerodrome();
		return aerodromiPraceni;
	}

	/**
	 * Postavlja  aerodromi praceni.
	 *
	 * @param aerodromiPraceni  aerodromi praceni
	 */
	public void setAerodromiPraceni(List<AerodromiPraceni> aerodromiPraceni) {
		this.aerodromiPraceni = aerodromiPraceni;
	}
	

	/**
	 * Dohvaca  polasci.
	 *
	 * @return  polasci
	 */
	public List<AvionLeti> getPolasci() {
		this.polasci = dajPolaske(icao, datum);
		return polasci;
	}

	/**
	 * Postavlja  polasci.
	 *
	 * @param polasci  new polasci
	 */
	public void setPolasci(List<AvionLeti> polasci) {
		this.polasci = polasci;
	}

	/**
	 * Dohvaca  dolasci.
	 *
	 * @return  dolasci
	 */
	public List<AvionLeti> getDolasci() {
		this.dolasci = dajDolaske(icao, datum);
		return dolasci;
	}

	/**
	 * Postavlja  dolasci.
	 *
	 * @param dolasci  dolasci
	 */
	public void setDolasci(List<AvionLeti> dolasci) {
		this.dolasci = dolasci;
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
	 * @param icao  icao
	 */
	public void setIcao(String icao) {
		this.icao = icao;
		mw.setIcao(icao);
	}
	
	/**
	 * Dohvaca  datum.
	 *
	 * @return  datum
	 */
	public String getDatum() {
		return datum;
	}

	/**
	 * Postavlja  datum.
	 *
	 * @param datum  datum
	 */
	public void setDatum(String datum) {
		this.datum = datum;
	}
	
	/**
	 * Dohvaca  mw.
	 *
	 * @return  mw
	 */
	public MeteoWs getMw() {
		return mw;
	}
	
	/**
	 * Dohvaca  dolasci polasci.
	 *
	 * @return  dolasci polasci
	 */
	public List<AvionLeti> getDolasciPolasci() {
		this.dolasciPolasci = dajDolaskePolaske(icao, datum);
		return dolasciPolasci;
	}


}
