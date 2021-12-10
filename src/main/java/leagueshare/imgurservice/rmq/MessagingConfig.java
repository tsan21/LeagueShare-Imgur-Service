package leagueshare.imgurservice.rmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;


@Configuration
public class MessagingConfig {

    static final String topicExchangeName = "leagueshare-exchanger";
    static final String queueName = "user-queue";
    public static final String routingKey = "leagueshare-key";


    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}
