package org.foi.nwtis.bsikac.zadaca_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.foi.nwtis.bsikac.vjezba_03.konfiguracije.Konfiguracija;
import org.foi.nwtis.bsikac.vjezba_03.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa Meduspremnik.
 */
public class Meduspremnik implements Serializable {

	
	
	/** Lista koja sadrzi {@link Komad}. */
	public List<Komad> spremnik;

	/**
	 * Klasa Komad.
	 */
	public class Komad {
		
		/** Zahtjev i odgovor koji se koriste za pohranu zahtjeva i odgovora 
		 * koji se koriste u radu servera {@link ServerGlavni}. */
		public String zahtjev, odgovor;
		
		/** Broj pojava odredenog zahtjeva i odgovora. */
		public int pojave = 0;

		/**
		 * Konstruktor klase {@link Komad}.
		 *
		 * @param zahtjev 	zahtjev
		 * @param odgovor 	odgovor
		 */
		public Komad(String zahtjev, String odgovor) {
			super();
			this.zahtjev = zahtjev;
			this.odgovor = odgovor;
			this.pojave += 1;
		}
	}

	/**
	 * Konstruktor klase {@link Meduspremnik}.
	 */
	public Meduspremnik() {
		super();
		spremnik = new ArrayList<Komad>();
	}

	/**
	 * Sortiraj spremnik.
	 */
	public void sortirajSpremnik() {
		for (Komad k : spremnik) {
			for (Komad k2 : spremnik) {
				if (k.pojave < k2.pojave) {
					Komad pom = k;
					k = k2;
					k2 = pom;
				}
			}
		}
	}

	/**
	 * Dodaj novi zahtjev u {@link #spremnik}.
	 *
	 * @param zahtjev 	zahtjev
	 * @param odgovor 	odgovor
	 */
	public void dodaj(String zahtjev, String odgovor) {
		if(odgovor.contains("OK"))
		{
			spremnik.add(new Komad(zahtjev, odgovor));
		}
	}

	/**
	 * Pronadji zahtjev u {@link #spremnik}.
	 *
	 * @param zahtjev 	zahtjev
	 * @return 			string
	 */
	public String pronadji(String zahtjev) {
		for (Komad k : spremnik) {
			if (k.zahtjev.equals(zahtjev)) {
				k.pojave += 1;
				return k.odgovor;
			}
		}
		return null;
	}

	/**
	 * Pisi u binarnu datoteku klasu {@link Meduspremnik}.
	 *
	 * @param nazivDatoteke 	naziv datoteke.
	 * @return true, 			ako je uspjesno.
	 */
	public boolean pisiUBin(String nazivDatoteke) {
		File datoteka = new File(nazivDatoteke);

		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(datoteka));
			oos.writeObject(this);
			oos.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * Citaj iz binarne datoteke.
	 *
	 * @param nazivDatoteke 	naziv datoteke.
	 * @return 					meduspremnik.
	 */
	public Meduspremnik citajIzBin(String nazivDatoteke) {
		String tip = Konfiguracija.dajTipKonfiguracije(nazivDatoteke);

		if (tip == null || tip.compareTo("bin") != 0) {
			System.out.println("Datoteka " + nazivDatoteke + " nije tipa " + "bin");
			return  null;
		}

		File datoteka = new File(nazivDatoteke);
		if (datoteka == null || !datoteka.isFile() || !datoteka.exists() || !datoteka.canRead()) {
			System.out.println("Datoteka " + nazivDatoteke + "  ne postoji ili se ne može čitati!");
			return  null;
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(datoteka))) {
			Object o = ois.readObject();
			if (o instanceof Meduspremnik)
				return (Meduspremnik) o;
			else
				System.out.println("Datoteka " + nazivDatoteke + " ne sadrži objekt tipa Properties!");
		} catch (IOException | ClassNotFoundException e) {
			System.out.println();
			return  null;
		}
		return null;
	}
	
	/**
	 * Ocisti {@link #spremnik}.
	 */
	public void clear()
	{
		this.spremnik.clear();
	}


}
