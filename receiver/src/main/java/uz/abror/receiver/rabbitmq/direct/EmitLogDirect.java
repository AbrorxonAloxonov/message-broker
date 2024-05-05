package uz.abror.receiver.rabbitmq.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String oddQueue = "odd_numbers_queue";
        channel.queueDeclare(oddQueue, false, false, false, null);
        channel.queueBind(oddQueue, EXCHANGE_NAME, "odd");

        String evenQueue = "even_numbers_queue";
        channel.queueDeclare(evenQueue, false, false, false, null);
        channel.queueBind(evenQueue, EXCHANGE_NAME, "even");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received '" + message + "' on queue '" + delivery.getEnvelope().getRoutingKey() + "'");
        };

        channel.basicConsume(oddQueue, true, deliverCallback, consumerTag -> {});

        channel.basicConsume(evenQueue, true, deliverCallback, consumerTag -> {});
    }
}
