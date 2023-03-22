package org.foi.nwtis.bsikac.zadaca_3.slusaci;

import java.io.File;

import org.foi.nwtis.bsikac.zadaca_3.dretve.Osvjezivac;
import org.foi.nwtis.bsikac.vjezba_03.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.KonfiguracijaBP;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * SlusacAplikacije.
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {


    /** Konfiguracija dbKonf. */
    private static PostavkeBazaPodataka dbKonf = null;

    /**
     * Stvara kontekst aplikacije.
     *
     * @param sce sce
     */
    @Override
	public void contextInitialized(ServletContextEvent sce) {
    	ServletContext context = sce.getServletContext();
    	String nazivDatoteke = context.getInitParameter("konfiguracija");
    	String putanja = context.getRealPath("/WEB-INF");
    	nazivDatoteke = putanja + File.separator + nazivDatoteke;
    	
    	System.out.println(nazivDatoteke);
    	
    	dbKonf = new PostavkeBazaPodataka(nazivDatoteke);
    	KonfiguracijaBP konfig = dbKonf;
    	try {
			konfig.ucitajKonfiguraciju();
		} catch (NeispravnaKonfiguracija e) {
			e.printStackTrace();
			return;
		}
    	
    	System.out.println("Konfiguracija učitana!");
    	context.setAttribute("postavke", konfig);
    	
    	Osvjezivac pra = new Osvjezivac();
    	pra.start();
    	
		ServletContextListener.super.contextInitialized(sce);
	}
    
    
	/**
	 * Unistava kontekst aplikacije.
	 *
	 * @param sce sce
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		context.removeAttribute("postavke");
		System.out.println("postavke obrisane!");
		ServletContextListener.super.contextDestroyed(sce);
	} 
	
	/**
	 * Dohvaca dbKonf.
	 *
	 * @return dbKonf
	 */
	public static PostavkeBazaPodataka getDbKonf() {
        return dbKonf;
    }
}
