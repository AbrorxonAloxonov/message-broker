package uz.abror.receiver.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class HeadersExchangeReceiver {

    private static final String EXCHANGE_NAME = "headers_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare the headers exchange
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);

            // Queue setup
            String queueName = channel.queueDeclare().getQueue();

            // Headers to match for binding
            Map<String, Object> headers = new HashMap<>();
            headers.put("type", "greeting");
            headers.put("format", "text");

            // Bind the queue to the exchange with specific headers
            channel.queueBind(queueName, EXCHANGE_NAME, "", headers);

            System.out.println(" [*] Waiting for messages with headers: " + headers);

            // Consumer setup
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        }
    }
}
