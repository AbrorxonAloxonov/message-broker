package uz.abror.sender.rabbitmq.spring.firstWork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class RabbitAmqpTutorialsRunner implements CommandLineRunner {

    @Value("10000")
    private int duration;

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Override
    public void run(String... arg0) throws Exception {
        System.out.println("Ready ... running for " + duration + "ms");
        Thread.sleep(duration);
        ctx.close();
    }
}
