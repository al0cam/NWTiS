package org.foi.nwtis.bsikac.zadaca_3.modeli;

import lombok.Getter;
import lombok.Setter;

/**
 * Klasa AerodromiProblemi.
 */
public class AerodromiProblemi {

		/**
		 * Vraca ident.
		 *
		 * @return ident
		 */
		@Getter
		
		/**
		 * Postavlja ident.
		 *
		 * @param ident  ident
		 */
		@Setter
		String ident;
		
		/**
		 * Vraca opis.
		 *
		 * @return description
		 */
		@Getter
		
		/**
		 * Postavlja opis.
		 *
		 * @param description  description
		 */
		@Setter
		String description;
		
		/**
		 * Instancira aerodromi problemi.
		 */
		public AerodromiProblemi() {
			
		}
}
