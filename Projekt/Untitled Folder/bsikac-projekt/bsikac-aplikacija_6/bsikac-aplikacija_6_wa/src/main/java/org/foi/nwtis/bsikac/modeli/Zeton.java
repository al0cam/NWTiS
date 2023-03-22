package org.foi.nwtis.bsikac.modeli;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Zeton {

	@Getter
	@Setter
	private int zeton;
	@Getter
	@Setter
	private String korisnik;
	@Getter
	@Setter
	private String lozinka;
	@Getter
	@Setter
	private int status;
	@Getter
	@Setter
	private long vrijeme;
	
	
	public boolean aktivan()
	{
		long trenutnoVrijeme = System.currentTimeMillis();
		if((vrijeme < trenutnoVrijeme) && status == 1)
			return true;
		else 
			return false;
	}
}
