package org.foi.nwtis.bsikac.aplikacija_4.modeli;

import org.foi.nwtis.podaci.Korisnik;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Uloga {
	@Getter
	@Setter
	Grupa grupa;
	@Getter
	@Setter
	Korisnik korisnik;
}
