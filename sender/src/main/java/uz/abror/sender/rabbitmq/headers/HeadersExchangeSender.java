package uz.abror.sender.rabbitmq.headers;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class HeadersExchangeSender {

    private static final String EXCHANGE_NAME = "headers_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare the headers exchange
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);

            // Numbers to send
            for (int i = 1; i <= 5; i++) {
                // Convert number to string for message content
                String message = String.valueOf(i);

                // Headers to match for routing
                Map<String, Object> headers = new HashMap<>();
                headers.put("type", "number");
                headers.put("value", i);

                // Publishing the message with headers
                AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                        .headers(headers)
                        .build();

                channel.basicPublish(EXCHANGE_NAME, "", props, message.getBytes());
                System.out.println(" [x] Sent '" + message + "' with headers: " + headers);
            }
        }
    }
}

