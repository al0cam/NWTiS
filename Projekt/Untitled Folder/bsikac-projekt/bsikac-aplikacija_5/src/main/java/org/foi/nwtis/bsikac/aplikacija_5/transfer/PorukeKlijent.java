package org.foi.nwtis.bsikac.aplikacija_5.transfer;

import org.foi.nwtis.bsikac.aplikacija_5.modeli.MeteoPodaciJMS;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConnectionFactoryDefinition;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.Session;

@JMSDestinationDefinition(name = "java:app/jms/NWTiS_bsikac", interfaceName = "jakarta.jms.Queue", destinationName = "NWTiS_bsikac")
@JMSConnectionFactoryDefinition(name = "java:app/jms/NWTiSQueueFactory")
public class PorukeKlijent {

	@Resource(mappedName = "jms/NWTiSQueueFactory")
	private static ConnectionFactory connectionFactory;
	@Resource(lookup = "jms/NWTiS_bsikac")
	private static Queue queue;
	

	public static boolean posaljiJMSPoruku(MeteoPodaciJMS meteoPodaci) {
		boolean status = true;
		try {
			Connection connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(queue);
			ObjectMessage message = session.createObjectMessage();

			message.setObject(meteoPodaci);
			messageProducer.send(message);
			messageProducer.close();
			connection.close();
		} catch (JMSException ex) {
			ex.printStackTrace();
			status = false;
		}
		return status;
	}
}
