package org.foi.nwtis.bsikac.aplikacija_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 *  Klasa AerodromDAO.
 */
public class AerodromDAO {
	
	/**  Konfiguracija dbKonf. */
	private static PostavkeBazaPodataka dbKonf = null;
	
	/**
	 * Dohvati aerodrom po icau.
	 *
	 * @param aerodrom  icao
	 * @return  aerodrom
	 */
	public static Aerodrom dohvatiAerodrom(String aerodrom) {
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
	            	
	            	Aerodrom k = new Aerodrom();
	            	k.setIcao(rs.getString("ident"));
	            	k.setDrzava(rs.getString("iso_country"));
	            	k.setNaziv(rs.getString("name"));
	            	
	                String coordinates = rs.getString("coordinates");
	            	String[] koo = coordinates.split(",");
	            	k.setLokacija(new Lokacija(koo[0],koo[1]));
	            	

	                return k;
	            } else {
	                throw new Exception();
	            }
	        } 
	        catch (Exception ex) {
	            ex.printStackTrace();            
	        }
	        return null;
	}
	
	/**
	 * Dohvati sve aerodrome.
	 *
	 * @return  aerodrom[]
	 */
	public static Aerodrom[] dohvatiSveAerodrome() {
        Aerodrom[] pAirport= null;

        try (
                 Connection con = otvoriVezu();
                 Statement stmt = con.createStatement();
        	) {
            String selectTable = "SELECT * FROM AIRPORTS";
            

            ResultSet rs = stmt.executeQuery(selectTable);
            ArrayList<Aerodrom> aAirport = new ArrayList<>();
            while (rs.next()) {
            	Aerodrom k = new Aerodrom();
            	k.setIcao(rs.getString("ident"));
            	k.setDrzava(rs.getString("iso_country"));
            	k.setNaziv(rs.getString("name"));
            	
                String coordinates = rs.getString("coordinates");
            	String[] koo = coordinates.split(",");
            	k.setLokacija(new Lokacija(koo[0],koo[1]));
            	
                aAirport.add(k);
            }
            pAirport = new Aerodrom[aAirport.size()];
            int i = 0;
            for (Aerodrom korisnik : aAirport) {
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
