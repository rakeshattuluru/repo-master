package info.source4code.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnidentifiedProducer {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(UnidentifiedProducer.class);

    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;

    public void create() throws JMSException {

        // create a Connection Factory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_BROKER_URL);

        // create a Connection
        connection = connectionFactory.createConnection();

        // create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // create a Message Producer for sending messages
        messageProducer = session.createProducer(null);
    }

    public void close() throws JMSException {
        connection.close();
    }

    public void sendName(String destinationName, String firstName,
            String lastName) throws JMSException {

        String text = firstName + " " + lastName;

        // create a JMS TextMessage
        TextMessage textMessage = session.createTextMessage(text);

        // create the Destination to which messages will be sent
        Destination destination = session.createQueue(destinationName);

        // send the message to the queue destination
        messageProducer.send(destination, textMessage);

        LOGGER.debug("unidentifiedProducer sent message with text='{}'", text);
    }
}