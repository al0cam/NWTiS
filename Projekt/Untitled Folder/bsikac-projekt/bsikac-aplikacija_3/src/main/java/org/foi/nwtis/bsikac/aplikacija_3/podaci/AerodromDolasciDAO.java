package org.foi.nwtis.bsikac.aplikacija_3.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 *  Klasa AerodromDolasciDAO.
 */
public class AerodromDolasciDAO {
	/**  Konfiguracija dbKonf. */
	private static PostavkeBazaPodataka dbKonf = null;
	
	public static AvionLeti[] dohvatiAerodromDolaskZaIcaoUIntervalu(String icao, long vrijemeOd, long vrijemeDo) {
		AvionLeti[] pAirport= null;
        String selectTable = "SELECT * FROM AERODROMI_DOLASCI ad WHERE ad.ESTARRIVALAIRPORT = ? AND ad.LASTSEEN BETWEEN ? AND ?";
        try (
                 Connection con = otvoriVezu();
                 PreparedStatement stmt = con.prepareStatement(selectTable);) {
        	stmt.setString(1, icao);
        	stmt.setLong(2, vrijemeOd);
        	stmt.setLong(3, vrijemeDo);
            ResultSet rs = stmt.executeQuery();
            ArrayList<AvionLeti> aAirport = new ArrayList<>();
            while (rs.next()) {
            	AvionLeti k = new AvionLeti();
	                
                k.setArrivalAirportCandidatesCount(rs.getInt("arrivalAirportCandidatesCount"));
                k.setCallsign(rs.getString("callSign"));
                k.setDepartureAirportCandidatesCount(rs.getInt("departureAirportCandidatesCount"));
                k.setEstArrivalAirport(rs.getString("estArrivalAirport"));
                k.setEstArrivalAirportHorizDistance(rs.getInt("estArrivalAirportHorizDistance"));
                k.setEstArrivalAirportVertDistance(rs.getInt("estArrivalAirportVertDistance"));
                k.setEstDepartureAirport(rs.getString("estDepartureAirport"));
                k.setEstDepartureAirportHorizDistance(rs.getInt("estDepartureAirportHorizDistance"));
                k.setEstDepartureAirportVertDistance(rs.getInt("estDepartureAirportVertDistance"));
                k.setFirstSeen(rs.getInt("firstSeen"));
                k.setLastSeen(rs.getInt("lastSeen"));
                k.setIcao24(rs.getString("icao24"));
                
                aAirport.add(k);
            }
            pAirport = new AvionLeti[aAirport.size()];
            int i = 0;
            for (AvionLeti korisnik : aAirport) {
                pAirport[i++] = korisnik;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pAirport;
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
