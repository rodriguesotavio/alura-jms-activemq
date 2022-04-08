package activemq.queue;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class TesteFila {

    // Sender, Receiver
    public static void main(String[] args) throws NamingException, JMSException {
//        Properties properties = new Properties();
//        properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
//        properties.setProperty("java.naming.provider.url", "tcp://localhost:61616");
//        properties.setProperty("queue.financeiro", "fila.financeiro");
//
//        InitialContext context = new InitialContext(properties);

        InitialContext context = new InitialContext();

        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Session session = connection.createSession(true, Session.SESSION_TRANSACTED);

        Destination fila = (Destination) context.lookup("financeiro");

        exemploProdutor(session, fila, "Olá, mundo!");

        session.commit();

        exemploConsumidor(session, fila);

        session.close();
        connection.close();
        context.close();
    }

    private static void exemploProdutor(Session session, Destination fila, String textMessage) throws JMSException {
        MessageProducer producer = session.createProducer(fila);
        Message message = session.createTextMessage(textMessage);
        producer.send(message);
    }

    private static void exemploConsumidor(Session session, Destination fila) throws JMSException {
        MessageConsumer consumer = session.createConsumer(fila);

        /**
         * O JMS segue o mesmo padrão de projeto Observer!
         * A diferença é que JMS é remoto ou distribuído. Ou seja,
         * no padrão Observer originalmente descrito no livro GOF,
         * tudo acontece na memória, o Observer desacopla objetos.
         * Com JMS, a comunicação entre Producer e Consumer é remota,
         * desacoplamento arquitetural.
         */

        consumer.setMessageListener((Message message) -> {
            TextMessage textMessage = (TextMessage) message;

//            try {
//                // Confirma o recebimento da mensagem
//                message.acknowledge();
//            } catch (JMSException e) {
//                throw new RuntimeException(e);
//            }

            try {
                System.out.println(textMessage.getText());
                session.commit();
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        });

        new Scanner(System.in).nextLine();
    }

}
