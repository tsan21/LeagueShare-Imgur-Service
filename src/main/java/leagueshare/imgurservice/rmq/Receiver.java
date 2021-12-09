package leagueshare.imgurservice.rmq;

import java.util.concurrent.CountDownLatch;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "user-queue")
public class Receiver {

    private final CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(byte[] message) {
        String strMessage = new String(message);
        System.out.println(strMessage);
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
