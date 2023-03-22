package org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka;

import java.util.Properties;

import org.foi.nwtis.bsikac.vjezba_03.konfiguracije.Konfiguracija;
import org.foi.nwtis.bsikac.vjezba_03.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.bsikac.vjezba_03.konfiguracije.NeispravnaKonfiguracija;

public class PostavkeBazaPodataka extends KonfiguracijaApstraktna 
					implements KonfiguracijaBP{

	public PostavkeBazaPodataka(String nazivDatoteke) {
		super(nazivDatoteke);
	}

	public String getAdminDatabase() {
		return this.dajPostavku("admin.database");
	}

	public String getAdminPassword() {
		return this.dajPostavku("admin.password");
	}

	public String getAdminUsername() {
		return this.dajPostavku("admin.username");
	}

	public String getDriverDatabase() {
		return "org.hsqldb.jdbcDriver";
	}

	public String getDriverDatabase(String urlBazePodataka) {
		// TODO Auto-generated method stub
		return null;
	}

	public Properties getDriversDatabase() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServerDatabase() {
		return this.dajPostavku("server.database");
	}

	public String getUserDatabase() {
		return this.dajPostavku("user.database");
	}

	public String getUserPassword() {
		return this.dajPostavku("user.password");
	}

	public String getUserUsername() {
		return this.dajPostavku("user.username");
	}

	@Override
	public void ucitajKonfiguraciju(String nazivDatoteke) throws NeispravnaKonfiguracija {
		Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
		this.postavke = konfig.dajSvePostavke();
	}

	@Override
	public void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija {
		Konfiguracija  konfig = KonfiguracijaApstraktna.dajKonfiguraciju(datoteka);
		Properties prop = this.dajSvePostavke();
		for (Object kljuc : prop.keySet()) {
			String k =  (String) kljuc;
			String v = this.dajPostavku(k);
			konfig.spremiPostavku(k, v);
		}
		konfig.spremiKonfiguraciju();
	}
}
