package org.foi.nwtis.bsikac.aplikacija_3.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.foi.nwtis.bsikac.aplikacija_3.modeli.AerodromiProblemi;
import org.foi.nwtis.bsikac.aplikacija_3.modeli.Grupa;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Class AerodromProblemDAO.
 */
public class KorisniciDAO {

	/** Konfiguracija dbKonf. */
	private static PostavkeBazaPodataka dbKonf = null;

	public static Korisnik[] dohvatiKorisnike() {
		Korisnik[] rezultat = null;

		String komanda = "SELECT * FROM KORISNICI;";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda);) {

			ResultSet rs = stmt.executeQuery();
			
			ArrayList<Korisnik> zet = new ArrayList<>();
			while (rs.next()) {
				Korisnik k = new Korisnik();
				k.setIme(rs.getString("ime"));
				k.setPrezime(rs.getString("prezime"));
				k.setEmail(rs.getString("email"));
				k.setLozinka(rs.getString("lozinka"));
				k.setKorIme(rs.getString("korisnik"));
				zet.add(k);
			}
			rezultat = new Korisnik[zet.size()];
			int i = 0;
			for (Korisnik z : zet) {
				rezultat[i++] = z;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rezultat;
	}
	
	public static Korisnik dohvatiKorisnika(String korisnik) {
		Korisnik k = null;

		String komanda = "SELECT * FROM KORISNICI WHERE KORISNIK = ?;";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda);) {
			stmt.setString(1, korisnik);

			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
				k = new Korisnik();
				k.setIme(rs.getString("ime"));
				k.setPrezime(rs.getString("prezime"));
				k.setEmail(rs.getString("email"));
				k.setLozinka(rs.getString("lozinka"));
				k.setKorIme(rs.getString("korisnik"));
				return k;
			}
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return k;
	}
	
	public static Korisnik dohvatiKorisnikaSaSifrom(String korisnik, String lozinka) {
		Korisnik k = null;

		String komanda = "SELECT * FROM KORISNICI WHERE KORISNIK = ? AND LOZINKA = ?;";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda);) {
			stmt.setString(1, korisnik);
			stmt.setString(2, lozinka);
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
				k = new Korisnik();
				k.setIme(rs.getString("ime"));
				k.setPrezime(rs.getString("prezime"));
				k.setEmail(rs.getString("email"));
				k.setLozinka(rs.getString("lozinka"));
				k.setKorIme(rs.getString("korisnik"));
				return k;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static boolean dodajKorisnika(Korisnik korisnik) {
		int k = 0;
		
		String komanda = "INSERT INTO KORISNICI VALUES(?,?,?,?,?);";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda);) {
			stmt.setString(1, korisnik.getKorIme());
			stmt.setString(2, korisnik.getIme());
			stmt.setString(3, korisnik.getPrezime());
			stmt.setString(4, korisnik.getLozinka());
			stmt.setString(5, korisnik.getEmail());
			k = stmt.executeUpdate();
			if(k==1)
				return true;
		} catch (Exception ex) {
		}
		return false;
	}

	
	public static Grupa[] dohvatiGrupeZaKorisnika(Korisnik korisnik) {
		Grupa[] rezultat = null;

		String komanda = "SELECT g.GRUPA, g.NAZIV  FROM GRUPE g, ULOGE u WHERE u.KORISNIK = ? AND g.GRUPA = u.GRUPA ;";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda);) {
			stmt.setString(1, korisnik.getKorIme());
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<Grupa> zet = new ArrayList<>();
			while (rs.next()) {
				Grupa k = new Grupa();
				k.setGrupa(rs.getString("grupa"));
				k.setNaziv(rs.getString("naziv"));
				
				zet.add(k);
			}
			rezultat = new Grupa[zet.size()];
			int i = 0;
			for (Grupa z : zet) {
				rezultat[i++] = z;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rezultat;
	}

	/**
	 * Otvori vezu.
	 *
	 * @return connection
	 * @throws ClassNotFoundException class not found exception
	 * @throws Exception              exception
	 * @throws SQLException           SQL exception
	 */
	private static Connection otvoriVezu() throws ClassNotFoundException, Exception, SQLException {
		Connection con;

		if (dbKonf == null) {
			throw new Exception();
		}
		String url = dbKonf.getServerDatabase() + dbKonf.getUserDatabase();
		Class.forName(dbKonf.getDriverDatabase());
		con = DriverManager.getConnection(url, dbKonf.getUserUsername(), dbKonf.getUserPassword());
		return con;
	}

	/**
	 * Postavlja dbKonf.
	 *
	 * @param aDbKonf dbKonf
	 */
	public static void setDbKonf(PostavkeBazaPodataka aDbKonf) {
		dbKonf = aDbKonf;
	}
}
