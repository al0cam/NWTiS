package org.foi.nwtis.bsikac.zadaca_1;

import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.AllArgsConstructor;

/**
 * Klasa za aerodrom.
 */
//@AllArgsConstructor()
public class Aerodrom {
    
	/** Icao. */
	@Getter
    @Setter 
    @NonNull 
    String icao;
    
    /** Naziv. */
    @Getter
    @Setter 
    @NonNull 
    String naziv;
    
    /** GPS geografska sirina. */
    @Getter
    @Setter 
    @NonNull 
    String gpsGS;
    
    /** GPS geografska duzina. */
    @Getter
    @Setter 
    @NonNull 
    String gpsGD;
	
	/**
	 * Konstruktor klase {@link Aerodrom}.
	 *
	 * @param icao 		icao.
	 * @param naziv 	naziv.
	 * @param gpsGS 	gps GS.
	 * @param gpsGD 	gps GD.
	 */
	public Aerodrom(@NonNull String icao, @NonNull String naziv, @NonNull String gpsGS, @NonNull String gpsGD) {
		super();
		this.icao = icao;
		this.naziv = naziv;
		this.gpsGS = gpsGS;
		this.gpsGD = gpsGD;
	}
	
	
    
}	
