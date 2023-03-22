package org.foi.nwtis.bsikac.aplikacija_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Airport;

/**
 *  Class AirportDAO.
 */
public class AirportDAO {

	/**  db konf. */
	private static PostavkeBazaPodataka dbKonf = null;
	
	/**
	 * Dohvati airport.
	 *
	 * @param aerodrom  icao
	 * @return  airport
	 */
	private static Airport dohvatiAirport(String aerodrom) {
		 String upit = "SELECT * FROM AIRPORTS a WHERE a.IDENT  = ?;";
	        try (
	                 Connection con = otvoriVezu();
	                 PreparedStatement s = con.prepareStatement(upit)
	        	) {

	            if (aerodrom == null || aerodrom.length() == 0) {
	                throw new Exception();
	            }

	            s.setString(1, aerodrom);
	            ResultSet rs = s.executeQuery();
	            if (rs.next()) {
	                String ident = rs.getString("ident");
	                String type = rs.getString("type");
	                String name = rs.getString("name");
	                String municipality = rs.getString("municipality");
	                String local_code= rs.getString("local_code");
	                String iso_region = rs.getString("iso_region");
	                String iso_country = rs.getString("iso_country");
	                String iata_code = rs.getString("iata_code");
	                String gps_code = rs.getString("gps_code");
	                String elevation_ft = rs.getString("elevation_ft");
	                String coordinates = rs.getString("coordinate");
	                String continent = rs.getString("continent");

	                Airport k = new Airport(ident, type, name, elevation_ft, continent, iso_country, iso_region, municipality, gps_code, iata_code, local_code, coordinates);
	                return k;
	            } else {
	                throw new Exception();
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();            
	        }
	        return null;
	}
	
	/**
	 * Dohvati sve airporte.
	 *
	 * @return  airport[]
	 */
	public static Airport[] dohvatiSveAirporte() {
        Airport[] pAirport= null;

        try (
                 Connection con = otvoriVezu();
                 Statement stmt = con.createStatement();
        	) {
            String selectTable = "SELECT * FROM AIRPORTS";
            

            ResultSet rs = stmt.executeQuery(selectTable);
            ArrayList<Airport> aAirport = new ArrayList<>();
            while (rs.next()) {
            	String ident = rs.getString("ident");
                String type = rs.getString("type");
                String name = rs.getString("name");
                String municipality = rs.getString("municipality");
                String local_code= rs.getString("local_code");
                String iso_region = rs.getString("iso_region");
                String iso_country = rs.getString("iso_country");
                String iata_code = rs.getString("iata_code");
                String gps_code = rs.getString("gps_code");
                String elevation_ft = rs.getString("elevation_ft");
                String coordinates = rs.getString("coordinates");
                String continent = rs.getString("continent");

                Airport k = new Airport(ident, type, name, elevation_ft, continent, iso_country, iso_region, municipality, gps_code, iata_code, local_code, coordinates);
                
                aAirport.add(k);
            }
            pAirport = new Airport[aAirport.size()];
            int i = 0;
            for (Airport korisnik : aAirport) {
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
     * Postavi dbKonf.
     *
     * @param aDbKonf  dbKonf
     */
    public static void setDbKonf(PostavkeBazaPodataka aDbKonf) {
        dbKonf = aDbKonf;
    }
	
}
