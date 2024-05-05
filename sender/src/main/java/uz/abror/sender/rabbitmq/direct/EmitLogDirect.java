package uz.abror.sender.rabbitmq.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String oddQueue = "odd_numbers_queue";
            channel.queueDeclare(oddQueue, false, false, false, null);
            channel.queueBind(oddQueue, EXCHANGE_NAME, "odd");

            String evenQueue = "even_numbers_queue";
            channel.queueDeclare(evenQueue, false, false, false, null);
            channel.queueBind(evenQueue, EXCHANGE_NAME, "even");

            for (int i = 1; i <= 10; i++) {
                String routingKey = (i % 2 == 0) ? "even" : "odd";
                String message = String.valueOf(i);
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
                System.out.println("Sent '" + message + "' with routing key '" + routingKey + "'");
            }
        }
    }
}