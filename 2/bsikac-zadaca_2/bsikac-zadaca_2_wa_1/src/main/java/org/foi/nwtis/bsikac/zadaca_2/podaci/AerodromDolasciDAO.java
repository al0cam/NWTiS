package org.foi.nwtis.bsikac.zadaca_2.podaci;

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
	
	/**
	 * Dohvati aerodom dolazak.
	 *
	 * @param id  id
	 * @return  avion leti
	 */
	private static AvionLeti dohvatiAerodomDolazak(String id) {
		 String upit = "SELECT *"
	                + "FROM AERODROMI_DOLASCI ad WHERE ad.ID = ?";
	        try (
	                 Connection con = otvoriVezu();
	                 PreparedStatement s = con.prepareStatement(upit)) {

	            if (id == null || id.length() == 0) {
	                throw new Exception();
	            }

	            s.setString(1, id);
	            ResultSet rs = s.executeQuery();
	            if (rs.next()) {

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
	 * Dohvati sve aerodrom dolaske.
	 *
	 * @return  avion leti[]
	 */
	public static AvionLeti[] dohvatiSveAerodromDolaske() {
		AvionLeti[] pAirport= null;

        try (
                 Connection con = otvoriVezu();
                 Statement stmt = con.createStatement();) {

            String selectTable = "SELECT * FROM AERODROMI_DOLASCI";

            ResultSet rs = stmt.executeQuery(selectTable);
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
	 * Dohvati sve aerodrom dolaske na dan.
	 *
	 * @param datum  datum
	 * @return  avion leti[]
	 */
	public static AvionLeti[] dohvatiSveAerodromDolaskeNaDan(String datum) {
		AvionLeti[] pAirport= null;
        String selectTable = "SELECT * FROM AERODROMI_DOLASCI ad WHERE ad.FIRSTSEEN BETWEEN ? AND ?";
        try (
                 Connection con = otvoriVezu();
                 PreparedStatement stmt = con.prepareStatement(selectTable);) {
        	int s = pretvoriDatumUSekunde(datum);
        	stmt.setInt(1, s);
        	stmt.setInt(2, s+86400);
        	System.out.println("DATUM: "+s);
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
	 * Dodaj aerodrom dolazak.
	 *
	 * @param let  let
	 * @return  boolean
	 */
	public static Boolean dodajAerodromDolazak(AvionLeti let) {
		Boolean odgovor = false;
		String komanda = "INSERT INTO AERODROMI_DOLASCI "
        		+ "(ICAO24,FIRSTSEEN,ESTDEPARTUREAIRPORT,LASTSEEN,ESTARRIVALAIRPORT,CALLSIGN,ESTDEPARTUREAIRPORTHORIZDISTANCE,ESTDEPARTUREAIRPORTVERTDISTANCE,ESTARRIVALAIRPORTHORIZDISTANCE,ESTARRIVALAIRPORTVERTDISTANCE,DEPARTUREAIRPORTCANDIDATESCOUNT,ARRIVALAIRPORTCANDIDATESCOUNT,STORED) "
        		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
        try (
                 Connection con = otvoriVezu();
                 PreparedStatement stmt = con.prepareStatement(komanda);
        	){

            stmt.setString(1, let.getIcao24());
            stmt.setInt(2, let.getFirstSeen());
            stmt.setString(3, let.getEstDepartureAirport());
            stmt.setInt(4, let.getLastSeen());
            stmt.setString(5, let.getEstArrivalAirport());
            stmt.setString(6, let.getCallsign());
            stmt.setInt(7, let.getEstDepartureAirportHorizDistance());
            stmt.setInt(8, let.getEstDepartureAirportVertDistance());
            stmt.setInt(9, let.getEstArrivalAirportHorizDistance());
            stmt.setInt(10, let.getEstArrivalAirportVertDistance());
            stmt.setInt(11, let.getDepartureAirportCandidatesCount());
            stmt.setInt(12, let.getArrivalAirportCandidatesCount());

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
    
    /**
     * Pretvori datum U sekunde.
     *
     * @param datum  datum
     * @return  int
     * @throws ParseException  parse exception
     */
    public static int pretvoriDatumUSekunde(String datum) throws ParseException {
		String formatBezSati = "[0-3]\\d\\.[0-1]\\d\\.\\d{4}";
		String formatSaSatima = "[0-3]\\d\\.[0-1]\\d\\.\\d{4} [0-2]\\d:[0-6]\\d:[0-6]\\d";
		SimpleDateFormat sdf = null;
		if(datum.matches(formatBezSati))
			sdf = new SimpleDateFormat("dd.MM.yyyy");
		else if(datum.matches(formatSaSatima))
			sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
       
        Date date = sdf.parse(datum);
        return (int) (date.getTime()/1000);
    }
}
