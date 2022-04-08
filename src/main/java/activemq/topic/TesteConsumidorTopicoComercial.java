package activemq.topic;

import activemq.model.Pedido;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class TesteConsumidorTopicoComercial {

    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext context = new InitialContext();

        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.setClientID("comercial");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topico = (Topic) context.lookup("loja");

        MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");
        consumer.setMessageListener((Message message) -> {
            ObjectMessage objMessage = (ObjectMessage) message;

            try {
                Pedido pedido = (Pedido) objMessage.getObject();
                System.out.println(pedido.getValorTotal());
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }

//            try {
//                System.out.println(textMessage.getText());
//            } catch (JMSException e) {
//                throw new RuntimeException(e);
//            }
        });
        new Scanner(System.in).nextLine();

        // Mensagens não entregues: DLQ - Dead Letter Queue

        session.close();
        connection.close();
        context.close();
    }

}
