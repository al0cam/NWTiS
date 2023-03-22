package org.foi.nwtis.bsikac.aplikacija_3.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.foi.nwtis.bsikac.aplikacija_3.modeli.AerodromiProblemi;
import org.foi.nwtis.bsikac.aplikacija_3.modeli.Zeton;
import org.foi.nwtis.bsikac.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Class AerodromProblemDAO.
 */
public class ZetoniDAO {

	/** Konfiguracija dbKonf. */
	private static PostavkeBazaPodataka dbKonf = null;

	public static Zeton[] dohvatiZetoneZaKorisnika(Korisnik korisnik) {		
		Zeton[] rezultat = null;

		String komanda = "SELECT * FROM ZETONI z WHERE z.KORISNIK = ? and z.LOZINKA = ?;";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda);) {
			stmt.setString(1, korisnik.getKorIme());
			stmt.setString(2, korisnik.getLozinka());

			ResultSet rs = stmt.executeQuery();
			
			ArrayList<Zeton> zet = new ArrayList<>();
			while (rs.next()) {
				Zeton k = new Zeton();
				k.setZeton(rs.getInt("zeton"));
				k.setKorisnik(rs.getString("korisnik"));
				k.setLozinka(rs.getString("lozinka"));
				k.setStatus(rs.getInt("status"));
				k.setVrijediDo(rs.getLong("vrijediDo"));
				zet.add(k);
			}
			rezultat = new Zeton[zet.size()];
			int i = 0;
			for (Zeton z : zet) {
				rezultat[i++] = z;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rezultat;
	}
	
	public static Zeton dohvatiZeton(String zeton) {
		Zeton k = null;

		String komanda = "SELECT * FROM ZETONI z WHERE z.zeton = ?;";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda);) {
			stmt.setString(1, zeton);

			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
				k = new Zeton();
				k.setZeton(rs.getInt("zeton"));
				k.setKorisnik(rs.getString("korisnik"));
				k.setLozinka(rs.getString("lozinka"));
				k.setStatus(rs.getInt("status"));
				k.setVrijediDo(rs.getLong("vrijediDo"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return k;
	}
	
	
	public static Zeton dodajZeton(Korisnik korisnik) {
		Zeton k = null;
		long zetonTrajanje = Long.parseLong(dbKonf.dajPostavku("zeton.trajanje"));
		
		String komanda = "INSERT INTO ZETONI (KORISNIK,LOZINKA,VRIJEDIDO,`stored`) VALUES (?,?,?,CURRENT_TIMESTAMP);";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda,Statement.RETURN_GENERATED_KEYS);) {
			
			long trajanjeZetona = System.currentTimeMillis()/1000 + zetonTrajanje;
			System.out.println();
			stmt.setString(1, korisnik.getKorIme());
			stmt.setString(2, korisnik.getLozinka());
			stmt.setLong(3, trajanjeZetona);
			int stanje = stmt.executeUpdate();
			
            if( stanje == 1)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                String id = null;
                if(rs.next())
                {
                    id = rs.getString(1);
                    k = dohvatiZeton(id);
    				return k;
                }
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	
	public static Boolean deaktivirajZeton(Zeton zeton) {
		Boolean odgovor = false;
		String komanda = "UPDATE ZETONI SET status = 0 WHERE ZETON = ?;";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda);) {
			stmt.setInt(1, zeton.getZeton());
			odgovor = stmt.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return odgovor;
	}
	
	public static int deaktivirajSveZetone(Korisnik korisnik) {
		int odgovor = 0;
		String komanda = "UPDATE ZETONI SET status = 0 WHERE KORISNIK =  ?;";
		try (Connection con = otvoriVezu(); PreparedStatement stmt = con.prepareStatement(komanda);) {
			stmt.setString(1, korisnik.getKorIme());
			odgovor = stmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return odgovor;
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
