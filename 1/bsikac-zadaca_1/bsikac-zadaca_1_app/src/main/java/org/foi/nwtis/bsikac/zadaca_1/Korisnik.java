package org.foi.nwtis.bsikac.zadaca_1;

import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.AllArgsConstructor;

/**
 * Klasa za korisnika.
 */
//@AllArgsConstructor()
public class Korisnik {

	/** Prezime. */
	@Getter
	@Setter
	@NonNull
	private String prezime;
	
	/** Ime. */
	@Getter
	@Setter
	@NonNull
	private String ime;
	
	/** Korisnicko ime. */
	@Getter
	@Setter
	@NonNull
	private String korisnickoIme;
	
	/** Lozinka. */
	@Getter
	@Setter
	@NonNull
	private String lozinka;

	/**
	 * Konstruktor klase {@link Korisnik}.
	 *
	 * @param prezime 		prezime.
	 * @param ime 			ime.
	 * @param korisnickoIme korisnicko ime.
	 * @param lozinka 		lozinka.
	 */
	public Korisnik(@NonNull String prezime, @NonNull String ime, @NonNull String korisnickoIme,
			@NonNull String lozinka) {
		super();
		this.prezime = prezime;
		this.ime = ime;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
	}

	/**
	 * Dohvaca prezime.
	 *
	 * @return 	prezime.
	 */
	public String getPrezime() {
		return prezime;
	}

	/**
	 * Postavlja prezime.
	 *
	 * @param prezime 	prezime.
	 */
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	/**
	 * Dohvaca ime.
	 *
	 * @return 	ime.
	 */
	public String getIme() {
		return ime;
	}

	/**
	 * Postavlja ime.
	 *
	 * @param ime 	ime.
	 */
	public void setIme(String ime) {
		this.ime = ime;
	}

	/**
	 * Dohvaca korisnicko ime.
	 *
	 * @return 	korisnicko ime.
	 */
	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	/**
	 * Postavlja korisnicko ime.
	 *
	 * @param korisnickoIme 	korisnicko ime.
	 */
	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	/**
	 * Dohvaca lozinka.
	 *
	 * @return 	lozinka.
	 */
	public String getLozinka() {
		return lozinka;
	}

	/**
	 * Postavlja lozinka.
	 *
	 * @param lozinka 	lozinka.
	 */
	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
	
}
