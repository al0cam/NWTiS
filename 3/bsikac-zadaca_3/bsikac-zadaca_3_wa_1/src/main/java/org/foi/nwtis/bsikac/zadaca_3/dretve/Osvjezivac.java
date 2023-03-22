package org.foi.nwtis.bsikac.zadaca_3.dretve;

import java.util.Date;

import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.bsikac.zadaca_3.podaci.AerodromPraceniDAO;
import org.foi.nwtis.bsikac.zadaca_3.slusaci.SlusacAplikacije;
import org.foi.nwtis.bsikac.zadaca_3.wsock.Info;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;

/**
 * Klasa Osvjezivac.
 */
public class Osvjezivac extends Thread {

	/**  Kraj. */
	boolean kraj=false;
	
	/**  Vrijeme spavanja. */
	int vrijemeSpavanja = 0;
	
	/**  Info. */
	@Inject Info info;
	
	/**  Postavke baze podataka. */
	PostavkeBazaPodataka pbp; 
	
	/**
	 * Start.
	 */
	@Override
	public synchronized void start() {
		pbp = SlusacAplikacije.getDbKonf();
		vrijemeSpavanja= Integer.parseInt(pbp.dajPostavku("ciklus.spavanje"));
		super.start();
	}

	/**
	 * Run.
	 */
	@Override
	public void run() {
		AerodromPraceniDAO.setDbKonf(pbp);
		while(!kraj) {
			String vrijeme = (new Date()).toString();
			int brojAerodroma = AerodromPraceniDAO.dohvatiSveAerodromePracene().length;
			info.informiraj(vrijeme +" => "+brojAerodroma);
			
			try {
				sleep(vrijemeSpavanja);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		super.run();
	}

	/**
	 * Interrupt.
	 */
	@Override
	public void interrupt() {
		kraj=true;
		super.interrupt();
	}

}
