SELECT COUNT(id)  FROM AERODROMI_POLASCI ap ;
SELECT COUNT(id) FROM AERODROMI_DOLASCI ad ;
SELECT COUNT(id) FROM AERODROMI_PRACENI ap ;



SELECT FROM_UNIXTIME(firstSeen,"%m %d %Y") as 'mm/dd/YYYY' ,COUNT(id) as "BROJ POLZAKA"  FROM AERODROMI_POLASCI m group by 1 ORDER BY 1;

SELECT FROM_UNIXTIME(firstSeen,"%m %d %Y") as 'mm/dd/YYYY', estDepartureAirport  as "ICAO" ,COUNT(id) as "BROJ POLZAKA"  FROM AERODROMI_POLASCI m group by 1, 2 ORDER BY 1, 2; 

SELECT FROM_UNIXTIME(firstSeen,"%m %d %Y") as 'mm/dd/YYYY',COUNT(id) as "BROJ POLZAKA"   FROM AERODROMI_POLASCI m WHERE m.estDepartureAirport  ='LDZA' group by 1 ORDER by 1; 


SELECT FROM_UNIXTIME(m.lastSeen ,"%m %d %Y") as 'mm/dd/YYYY' ,COUNT(id) as "BROJ DOLZAKA"   FROM AERODROMI_DOLASCI m group by 1 ORDER by 1; 

SELECT FROM_UNIXTIME(m.lastSeen ,"%m %d %Y") as 'mm/dd/YYYY', estArrivalAirport as "ICAO" ,COUNT(id) as "BROJ DOLZAKA"  FROM AERODROMI_DOLASCI m group by 1, 2 ORDER by 1, 2; 

SELECT FROM_UNIXTIME(m.lastSeen ,"%m %d %Y") as 'mm/dd/YYYY', COUNT(id) as "BROJ DOLZAKA"   FROM AERODROMI_DOLASCI m WHERE m.estArrivalAirport ='LDZA' group by 1 ORDER by 1;

