package org.foi.nwtis.bsikac.zadaca_3.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.bsikac.zadaca_3.modeli.AerodromiProblemi;

/**
 *  Klasa AerodromProblemDAO.
 */
public class AerodromProblemDAO {
	
	/**  Konfiguracija dbKonf. */
	private static PostavkeBazaPodataka dbKonf = null;

	/**
	 * Dohvati sve aerodrom probleme za icao.
	 *
	 * @param icao  icao
	 * @return  aerodromi problemi[]
	 */
	public static AerodromiProblemi[] dohvatiSveAerodromProblemeZaIcao(String icao) {
		AerodromiProblemi[] pAirport = null;
		String upit = "SELECT * FROM  AERODROMI_PROBLEMI ap where ap.ident = ?;";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(upit);) {

			stmt.setString(1, icao);
			ResultSet rs = stmt.executeQuery();
			ArrayList<AerodromiProblemi> aAirport = new ArrayList<>();
			while (rs.next()) {
				AerodromiProblemi k = new AerodromiProblemi();

				k.setIdent(rs.getString("ident"));
				k.setDescription(rs.getString("description"));

				aAirport.add(k);
			}
			pAirport = new AerodromiProblemi[aAirport.size()];
			int i = 0;
			for (AerodromiProblemi korisnik : aAirport) {
				pAirport[i++] = korisnik;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return pAirport;
	}

	/**
	 * Dohvati sve aerodrom probleme.
	 *
	 * @return  aerodromi problemi[]
	 */
	public static AerodromiProblemi[] dohvatiSveAerodromProbleme() {
		AerodromiProblemi[] pAirport = null;

		try (Connection con = otvoriVezu(); Statement stmt = con.createStatement();) {

			String selectTable = "SELECT * FROM  AERODROMI_PROBLEMI ap;";

			ResultSet rs = stmt.executeQuery(selectTable);
			ArrayList<AerodromiProblemi> aAirport = new ArrayList<>();
			while (rs.next()) {
				AerodromiProblemi k = new AerodromiProblemi();

				k.setIdent(rs.getString("ident"));
				k.setDescription(rs.getString("description"));

				aAirport.add(k);
			}
			pAirport = new AerodromiProblemi[aAirport.size()];
			int i = 0;
			for (AerodromiProblemi korisnik : aAirport) {
				pAirport[i++] = korisnik;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return pAirport;
	}

	/**
	 * Dodaj aerodrom problem.
	 *
	 * @param let  let
	 * @return  boolean
	 */
	public static Boolean dodajAerodromProblem(AerodromiProblemi let) {
		Boolean odgovor = false;
		String komanda = "INSERT INTO AERODROMI_PROBLEMI (IDENT,DESCRIPTION,STORED) VALUES (?,?,CURRENT_TIMESTAMP);";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda);) {
			stmt.setString(1, let.getIdent());
			stmt.setString(2, let.getDescription());

			odgovor = stmt.execute();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return odgovor;
	}

	/**
	 * Obiris aerodrom problem.
	 *
	 * @param icao  icao
	 * @return  boolean
	 */
	public static Boolean obirisAerodromProblem(String icao) {
		Boolean odgovor = false;
		String komanda = "DELETE FROM AERODROMI_PROBLEMI ap WHERE ap.IDENT = ?;";
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
