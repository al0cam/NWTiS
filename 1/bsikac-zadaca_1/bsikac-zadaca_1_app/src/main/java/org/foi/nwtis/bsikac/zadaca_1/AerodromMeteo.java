package org.foi.nwtis.bsikac.zadaca_1;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Klasa za meteo zapis aerodroma.
 */
//@AllArgsConstructor()
public class AerodromMeteo implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** Icao. */
	@Getter
	@Setter
	@NonNull
	String icao;
	
	/** Temp. */
	@Getter
	@Setter
	double temp;
	
	/** Tlak. */
	@Getter
	@Setter
	double tlak;
	
	/** Vlaga. */
	@Getter
	@Setter
	double vlaga;
	
	/** Vrijeme prema vremenskoj prognozi. */
	@Getter
	@Setter
	@NonNull
	String vrijeme;
	
	/** Vrijeme prema satu. */
	@Getter
	@Setter
	long time;

	/**
	 * Konstruktor klase {@link AerodromMeteo}.
	 *
	 * @param icao 		icao.
	 * @param temp 		temp.
	 * @param tlak 		tlak.
	 * @param vlaga 	vlaga.
	 * @param vrijeme 	vrijeme.
	 * @param time 		time.
	 */
	public AerodromMeteo(@NonNull String icao, double temp, double tlak, double vlaga, @NonNull String vrijeme,
			long time) {
		super();
		this.icao = icao;
		this.temp = temp;
		this.tlak = tlak;
		this.vlaga = vlaga;
		this.vrijeme = vrijeme;
		this.time = time;
	}
	
}
