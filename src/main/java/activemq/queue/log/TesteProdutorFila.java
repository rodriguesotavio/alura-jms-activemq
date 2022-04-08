package activemq.queue.log;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TesteProdutorFila {
    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext context = new InitialContext();

        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination fila = (Destination) context.lookup("LOG");

        MessageProducer producer = session.createProducer(fila);

        Message logInfo = session.createTextMessage("INFO | Uma mensagem de log");
        producer.send(logInfo, DeliveryMode.NON_PERSISTENT, 3, 5000);

        Message logError = session.createTextMessage("ERROR | Falha crítica na aplicação");
        producer.send(logError, DeliveryMode.PERSISTENT, 9, 5000);

        session.close();
        connection.close();
        context.close();
    }

}
