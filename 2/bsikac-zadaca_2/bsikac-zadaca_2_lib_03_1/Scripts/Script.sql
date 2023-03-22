SELECT * FROM INFORMATION_SCHEMA.SYSTEM_USERS;

CREATE USER "nwtis_g1" PASSWORD "nwtis#g1";
CREATE USER "nwtis_g2" PASSWORD "nwtis#g2";
CREATE USER "nwtis_g3" PASSWORD "nwtis#g3";
CREATE USER "nwtis_g4" PASSWORD "nwtis#g4";

SELECT * FROM INFORMATION_SCHEMA.SYSTEM_USERS;

CREATE ROLE "aplikacija";

GRANT "aplikacija" TO "nwtis_g1";
GRANT "aplikacija" TO "nwtis_g2";
GRANT "aplikacija" TO "nwtis_g3";
GRANT "aplikacija" TO "nwtis_g4";

CREATE TABLE airports (
  ident varchar(10) NOT NULL,
  type varchar(30) NOT NULL,
  name varchar(255) NOT NULL,
  elevation_ft varchar(10),
  continent varchar(30),  
  iso_country varchar(30),  
  iso_region varchar(10),  
  municipality varchar(30),  
  gps_code varchar(10) NOT NULL,  
  iata_code varchar(10) NOT NULL,  
  local_code varchar(10) NOT NULL,  
  coordinates varchar(30) NOT NULL,  
  PRIMARY KEY (ident)
);

GRANT SELECT, UPDATE, INSERT ON TABLE airports TO "aplikacija";

SELECT * FROM INFORMATION_SCHEMA.TABLE_PRIVILEGES;

-- BRISANJE DOZVOLA, TABLICA, ULOGA, KORISNIKA

REVOKE SELECT, UPDATE, INSERT ON TABLE airports FROM "aplikacija" RESTRICT;

DROP TABLE airports;

DROP ROLE "aplikacija";

DROP USER "nwtis_g1";
DROP USER "nwtis_g2";
DROP USER "nwtis_g3";
DROP USER "nwtis_g4";