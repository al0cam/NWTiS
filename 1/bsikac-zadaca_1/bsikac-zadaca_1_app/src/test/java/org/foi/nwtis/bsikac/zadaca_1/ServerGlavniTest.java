package org.foi.nwtis.bsikac.zadaca_1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

import org.foi.nwtis.bsikac.vjezba_03.konfiguracije.Konfiguracija;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ServerGlavniTest {

	ServerGlavni serverGlavni = null;
	ServerMeteo serverMeteo = null;
	ServerAerodroma serverAerodroma = null;
	ServerUdaljenosti serverUdaljenosti = null;
	DretvaTest dMeteo;
	DretvaTest dUd;
	DretvaTest dAero;
	
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		serverGlavni= new ServerGlavni(8003, 10);
		dMeteo = new DretvaTest();
		dUd= new DretvaTest();
		dAero = new DretvaTest();
//		dg.start("glavni");
		dMeteo.start("meteo");
		dUd.start("ud");
		dAero.start("aero");
		
	}

	@AfterEach
	void tearDown() throws Exception {
		serverGlavni = null;
		serverMeteo = null;
		serverAerodroma = null;
		serverUdaljenosti = null;
		dMeteo.join();
		dUd.join();
		dAero.join();
	}

	@Test
	@Disabled
	void testMain() {
		fail("Not yet implemented");
	}

	@Test
	void testUcitajKonfiguraciju() {
		assertNull(serverGlavni.getKonfig());
		boolean odgovor = serverGlavni.ucitajKonfiguraciju("NWTiS_bsikac_4.txt");
		boolean ocekivano = true;
		assertEquals(ocekivano, odgovor);
		assertNotNull(serverGlavni.getKonfig());
		assertNotEquals(0, serverGlavni.getKonfig().dajSvePostavke().size());
		assertNotNull(serverGlavni.getKonfig().dajPostavku("port"));
	}

	@Test
	void testUcitajKorisnike() {
		assertEquals(0, serverGlavni.getKorisnici().size());
		serverGlavni.ucitajKorisnike("korisnici.csv");
		assertNotEquals(0, serverGlavni.getKorisnici().size());

	}

	@Test
	void testObradaZahtjeva() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 METEO LDZA";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testMeteoIcaoDatum() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 METEO LDVA 2021-01-07";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testTemp() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 TEMP 4,0 5,0";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testTempDatum() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 TEMP 4,0 5,0 2021-01-07";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testAirport() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 AIRPORT";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testAirportIcao() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 AIRPORT LDZA";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testAirportIcaoBrojKm() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 AIRPORT LDZA 500";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testDistance() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 DISTANCE LDZA LDVA";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testDistanceClear() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 DISTANCE CLEAR";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	@Test
	void testCacheBackup() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 CACHE BACKUP";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Test
	void testCacheRestore() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 CACHE RESTORE";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Test
	void testCacheClear() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 CACHE CLEAR";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testCacheStat() {
		DretvaTest dg = new DretvaTest();
		dg.start("glavni");

		
		String komanda = "USER ssokol PASSWORD 123456 CACHE STAT";
		String odgovor = "";
		odgovor = posaljiKomandu(komanda);
		System.out.println(odgovor);
		assertEquals("OK ", odgovor.substring(0, 3));
		
		try {
			dMeteo.join();
			dUd.join();
			dAero.join();
			dg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public class DretvaTest extends Thread {

		private String opcija = "obrada";
		
		public synchronized void start(String opcija) {
			super.start();
			this.opcija = opcija;
		}

		@Override
		public void run() {
			super.run();
			
			if(this.opcija.contains("obrada"))
			{
				serverGlavni.obradaZahtjeva();
			}else if(this.opcija.contains("meteo"))
			{
				pokreniServerMeteo();
			}else if(this.opcija.contains("aero"))
			{
				pokreniServerAerodroma();
			}else if(this.opcija.contains("ud"))
			{
				pokreniServerUdaljenosti();
			}else if(this.opcija.contains("glavni"))
			{
				pokreniGlavniServer();
			}
		}

		@Override
		public void interrupt() {
			// TODO Auto-generated method stub
			super.interrupt();
		}
	}
	
	
	
	public static String posaljiKomandu(String komanda)
	{
		String odgovor = null;
		try (Socket veza = new Socket("localhost", 8003);
				InputStreamReader isr = new InputStreamReader(veza.getInputStream(), Charset.forName("UTF-8"));
				OutputStreamWriter osw = new OutputStreamWriter(veza.getOutputStream(), Charset.forName("UTF-8"));) {

			osw.write(komanda);
			osw.flush();
			veza.shutdownOutput();
			StringBuilder tekst = new StringBuilder();
			while (true) {
				int i = isr.read();
				if (i == -1) {
					break;
				}
				tekst.append((char) i);
			}
			veza.shutdownInput();
			veza.close();
			return odgovor = tekst.toString();
		} catch (SocketException e) {
			System.out.println(e.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return odgovor;
	}

	
	public void pokreniGlavniServer()
	{
		String[] args = {"NWTiS_bsikac_4.txt"};
		ServerGlavni.main(args);
	}
	
	public void pokreniServerMeteo()
	{
		String[] args = {"NWTiS_bsikac_1.txt"};
		ServerMeteo.main(args);
	}
	
	public void pokreniServerAerodroma()
	{
		String[] args = {"NWTiS_bsikac_2.txt"};
		ServerAerodroma.main(args);
	}
	
	public void pokreniServerUdaljenosti()
	{
		String[] args = {"NWTiS_bsikac_3.txt"};
		ServerUdaljenosti.main(args);
	}
}
