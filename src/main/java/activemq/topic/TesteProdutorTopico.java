package activemq.topic;

import activemq.model.Pedido;
import activemq.model.PedidoFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.util.Scanner;

public class TesteProdutorTopico {

    // Publisher, Subscriber
    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext context = new InitialContext();

        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination topico = (Destination) context.lookup("loja");

        MessageProducer producer = session.createProducer(topico);

        Pedido pedido = new PedidoFactory().geraPedidoComValores();

//        StringWriter writer = new StringWriter();
//        JAXB.marshal(pedido, writer);
//
//        Message message = session.createTextMessage(writer.toString());
        Message message = session.createObjectMessage(pedido);
        message.setBooleanProperty("ebook", true);

        producer.send(message);

        session.close();
        connection.close();
        context.close();
    }

}
