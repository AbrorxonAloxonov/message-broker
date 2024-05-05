package uz.abror.receiver.rabbitmq.topic;

import com.rabbitmq.client.*;

public class TopicExchangeReceiver {

    private static final String EXCHANGE_NAME = "topic_exchange";
    private static final String QUEUE_NAME = "topic_queue";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        String[] routingKeys = {"animal.*", "*.red.*","auto.#"};
        for (String routingKey : routingKeys) {
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, routingKey);
            System.out.println("Bound queue '" + QUEUE_NAME + "' to exchange '" + EXCHANGE_NAME + "' with routing key '" + routingKey + "'");
        }

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println("Received '" + message + "' on queue '" + QUEUE_NAME + "' with routing key '" + routingKey + "'");
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}
