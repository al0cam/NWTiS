package org.foi.nwtis.bsikac.aplikacija_5.wsock;

import java.beans.JavaBean;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.foi.nwtis.bsikac.aplikacija_5.transfer.AerodromiKlijent;

import jakarta.inject.Singleton;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Klasa Info.
 */
@ServerEndpoint("/info")
@Singleton
public class Info {

	/** Sesije. */
	private Set<Session> sesije = new HashSet<Session>();
	
	
	public void dajInfo(String korisnik, String zeton)
	{
		long vrijeme = System.currentTimeMillis();
		long brojPracenihAerodroma = AerodromiKlijent.dohvatiBrojPracenihAerodroma(korisnik, zeton);
		informiraj("vrijeme: "+vrijeme+" \n Broj aerodroma za preuzimanje: "+brojPracenihAerodroma);
	}
	
	
	
	/**
	 * Otvori.
	 *
	 * @param sesija  sesija
	 * @param konfig  konfig
	 */
	@OnOpen	
	public void otvori(Session sesija, EndpointConfig konfig)
	{
		sesije.add(sesija);
		System.out.println("Otvorena sesija: "+sesija.getId());
		
	}
	
	/**
	 * Zatvori.
	 *
	 * @param sesija  sesija
	 * @param razlog  razlog
	 */
	@OnClose
	public void zatvori(Session sesija, CloseReason razlog)
	{
		sesije.remove(sesija);
		System.out.println("Zatvorena sesija: "+sesija.getId() + " razlog: "+razlog.getReasonPhrase());
	}
	
	/**
	 * Stigla poruka.
	 *
	 * @param sesija  sesija
	 * @param poruka  poruka
	 */
	@OnMessage
	public void stiglaPoruka(Session sesija, String poruka)
	{
		System.out.println("Otvorena sesija: "+sesija.getId() + " poruka: "+ poruka);
	}
	
	/**
	 * Greska.
	 *
	 * @param sesija  sesija
	 * @param greska  greska
	 */
	@OnError
	public void greska(Session sesija, Throwable greska)
	{
		System.out.println("Otvorena sesija: "+sesija.getId()+" greska: "+ greska.getMessage());
		
	}
	
	/**
	 * Informiraj.
	 *
	 * @param poruka  poruka
	 */
	public void informiraj(String poruka)
	{
		for(Session s: sesije)
		{
			try {
				s.getBasicRemote().sendText(poruka);
			} catch (IOException e) {
				System.out.println("Otvorena sesija: "+s.getId()+" greska: "+ e.getMessage());
			}
		}
		
	}
}
