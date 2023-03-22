package org.foi.nwtis.bsikac.zadaca_3.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.bsikac.zadaca_3.modeli.AerodromiPraceni;

/**
 *  Klasa AerodromPraceniDAO.
 */
public class AerodromPraceniDAO {
	
	/**  Konfiguracija dbKonf. */
	private static PostavkeBazaPodataka dbKonf = null;

	/**
	 * Dohvati sve aerodrome pracene.
	 *
	 * @return  aerodromi praceni[]
	 */
	public static AerodromiPraceni[] dohvatiSveAerodromePracene() {
		AerodromiPraceni[] pAirport = null;

		try (Connection con = otvoriVezu(); Statement stmt = con.createStatement();) {

			String selectTable = "SELECT * FROM  AERODROMI_PRACENI ap;";

			ResultSet rs = stmt.executeQuery(selectTable);
			ArrayList<AerodromiPraceni> aAirport = new ArrayList<>();
			while (rs.next()) {
				AerodromiPraceni k = new AerodromiPraceni();

				k.setIdent(rs.getString("ident"));

				aAirport.add(k);
			}
			pAirport = new AerodromiPraceni[aAirport.size()];
			int i = 0;
			for (AerodromiPraceni korisnik : aAirport) {
				pAirport[i++] = korisnik;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return pAirport;
	}

	/**
	 * Dodaj aerodrom praceni.
	 *
	 * @param let  let
	 * @return  boolean
	 */
	public static Boolean dodajAerodromPraceni(AerodromiPraceni let) {
		String komanda = "INSERT INTO AERODROMI_PRACENI (IDENT,STORED) VALUES (?,CURRENT_TIMESTAMP);";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda);) {
			stmt.setString(1, let.getIdent());
			stmt.execute();
			if(stmt.getUpdateCount()>0)
				return true;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * Obiris aerodrom praceni.
	 *
	 * @param icao  icao
	 * @return  boolean
	 */
	public static Boolean obirisAerodromPraceni(String icao) {
		Boolean odgovor = false;
		String komanda = "DELETE FROM AERODROMI_PRACENI ap WHERE ap.IDENT = ?;";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda);) {
			stmt.setString(1, icao);
			odgovor = stmt.execute();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return odgovor;
	}
	
	/**
	 * Otvori vezu.
	 *
	 * @return  connection
	 * @throws ClassNotFoundException  class not found exception
	 * @throws Exception  exception
	 * @throws SQLException  SQL exception
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
	 * @param aDbKonf  dbKonf
	 */
	public static void setDbKonf(PostavkeBazaPodataka aDbKonf) {
		dbKonf = aDbKonf;
	}
	
}
