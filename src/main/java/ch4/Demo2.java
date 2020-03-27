package ch4;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Demo2 {
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
        Map<String,Object> arg = new HashMap<String, Object>();
        arg.put("alternate-exchange","AE");
        channel.exchangeDeclare("normalExchange","direct",true,false,arg);
        channel.exchangeDeclare("AE","fanout",true,false,null);
        channel.queueDeclare("normalQueue",true,false,false,null);
        channel.queueBind("normalQueue","normalExchange","normalKey");
        channel.queueDeclare("unroutedQueue",true,false,false,null);
        channel.queueDeclare("unroutedQueue",true,false,false,null);

        channel.queueBind("unroutedQueue","AE","");
        Map<String,Object> queueArgs = new HashMap<String, Object>();
        queueArgs.put("x-expires",1800000);
        channel.queueDeclare("queueName",false,false,false,queueArgs);

        channel.exchangeDeclare("DLX_EXCHANGE","direct");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("X-dead-letter-exchange","DLX_EXCHANGE");
        channel.queueDeclare("queuename",false,false,false,map);


    }
}
