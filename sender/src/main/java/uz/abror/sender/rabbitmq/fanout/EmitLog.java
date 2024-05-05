package uz.abror.sender.rabbitmq.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        AtomicInteger atomicInteger = new AtomicInteger();


        Thread outputThread = new Thread(() -> {
            while (true) {
                try (Connection connection = factory.newConnection()) {
                    Channel channel = connection.createChannel();
                    channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
                    String message = "send  " + atomicInteger.getAndIncrement();

                    channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
                    System.out.println(" [x] Sent '" + message + "'");
                    Thread.sleep(1000);
                }catch (Exception e){
                    throw new RuntimeException();
                }
            }
        });
        outputThread.start();
    }
}
