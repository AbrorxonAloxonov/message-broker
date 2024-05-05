package uz.abror.sender.rabbitmq.topic;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class TopicExchangeSender {

    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            String message1 = "Hello bird";
            String routingKey1 = "animal.lion";
            sendMessage(channel, message1, routingKey1);

            String message2 = "Red fox spotted";
            String routingKey2 = "bird.red.fox";
            sendMessage(channel, message2, routingKey2);

            String message3 = "Jentra kotta bollani moshinasi";
            String routingKey3 = "auto.malibu.chevrolet";
            sendMessage(channel, message3, routingKey3);
        }
    }

    private static void sendMessage(Channel channel, String message, String routingKey) throws Exception {
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        System.out.println("Sent message '" + message + "' with routing key '" + routingKey + "'");
    }
}

