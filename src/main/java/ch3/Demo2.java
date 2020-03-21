package ch3;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Demo2 {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "39.106.79.70";
    private static final int PORT = 5672;
    public static void main(String[] args) throws IOException, TimeoutException,InterruptedException{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("rxxxx");
        factory.setPassword("xxxxxx");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("source","direct",false,true,null);
        channel.exchangeDeclare("destination","fanout",false,true,true,null);
        channel.exchangeBind("destination","source","exKey");
        channel.queueDeclare("queue",false,false,true,null);
        channel.queueBind("queue","destination","");
        channel.basicPublish("source","exKey",null,"exToDemo".getBytes());
        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY,new AMQP.BasicProperties().builder()
        .contentType("text/plain")
        .deliveryMode(2).priority(1).userId("hidden")
                .build(),
                "Hello World".getBytes()
        );

    }
}
