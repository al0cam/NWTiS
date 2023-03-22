package org.foi.nwtis.bsikac.zadaca_2.modeli;

import lombok.Getter;
import lombok.Setter;

/**
 * Class AerodromiProblemi.
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
		 * @param ident ident
		 */
		@Setter
		String ident;
		
		/**
		 * Vraca description.
		 *
		 * @return description
		 */
		@Getter
		
		/**
		 * Postavlja description.
		 *
		 * @param description new description
		 */
		@Setter
		String description;
		
		/**
		 * Instancira aerodromiProblemi.
		 */
		public AerodromiProblemi() {
			
		}
}
