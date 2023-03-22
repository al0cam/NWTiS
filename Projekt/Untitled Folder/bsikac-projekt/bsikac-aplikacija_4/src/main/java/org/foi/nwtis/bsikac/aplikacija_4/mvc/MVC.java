package org.foi.nwtis.bsikac.aplikacija_4.mvc;

import org.foi.nwtis.bsikac.aplikacija_4.modeli.Zeton;
import org.foi.nwtis.podaci.Korisnik;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Klasa MVC.
 */
@ApplicationPath("/mvc")
public class MVC extends Application{
	public static Zeton adminZeton = null;
	public static Zeton korisnickiZeton = null;
	public static Korisnik korisnik = null;
	public static boolean korisnikUGrupiZaBrisanje = false;
}
