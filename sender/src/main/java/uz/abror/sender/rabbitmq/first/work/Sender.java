package uz.abror.sender.rabbitmq.first.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {
    private static String QUEUE_NAME = "hello" ;

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Thread outputThread = new Thread(() -> {
            while (true) {
                System.out.println("Continuous output...");
                try (Connection connection = factory.newConnection();
                     Channel channel = connection.createChannel()) {
                    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                    String message = "Salom Qalesan ishlarin yaxshimi , hammasi yaxshi bop ketadi";
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                    System.out.println(" [x] Sent '" + message + "'");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        outputThread.start();

    }
}
