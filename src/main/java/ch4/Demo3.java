package ch4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Demo3 {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "39.106.79.70";
    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException,InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");
        factory.setPassword("zw19990209520");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("exchange.dlx","direct",true);
        channel.exchangeDeclare("exchange.normal","fanout",true);
        Map<String,Object> queueArgs = new HashMap<String, Object>();
        queueArgs.put("x-message-ttl",10000);
        queueArgs.put("x-dead-letter-exchange","exchange.dlx");
        queueArgs.put("x-dead-letter-routing-key","routingkey");
        channel.queueDeclare("queue.normal",true,false,false,queueArgs);
        channel.queueBind("queue.normal","exchange.normal","");
        channel.queueDeclare("queue.dlx",true,false,false,null);
        channel.queueBind("queue.dlx","exchange.dlx","routingkey");
        channel.basicPublish("exchange.normal","rk", MessageProperties.PERSISTENT_TEXT_PLAIN,"dlx".getBytes());


    }
}
