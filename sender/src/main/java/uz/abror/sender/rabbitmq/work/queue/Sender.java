package uz.abror.sender.rabbitmq.work.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class Sender {
    private static String QUEUE_NAME = "hello" ;

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        AtomicInteger i = new AtomicInteger();

        Thread outputThread = new Thread(() -> {
            while (true) {
                System.out.println("Continuous output...");
                try (Connection connection = factory.newConnection();
                     Channel channel = connection.createChannel()) {
                    boolean durable = true;
                    channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
                    String message = "send  " + i.getAndIncrement();
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                    System.out.println(" [x] Sent '" + message + "'");

                    Thread.sleep(100);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        outputThread.start();
    }
}
