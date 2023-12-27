package org.example.socials.youtube.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {

//    @Bean
//    DirectExchange storageExchange(@Value("${socials.rabbit.exchange.storage.name}") String exchangeName) {
//        return new DirectExchange(exchangeName);
//    }


    @Bean
    public DirectExchange youtubeSocialsStorageExchange() {
            return new DirectExchange("youtube-socials-storage");
    }

    @Bean
    public Queue socialsStorageQueue() {
        return new Queue("socials-storage");
    }

    @Bean
    public Binding youtubeStorageBinding(DirectExchange youtubeSocialsStorageExchange, Queue socialsStorageQueue) {
        return BindingBuilder.bind(socialsStorageQueue)
                .to(youtubeSocialsStorageExchange)
                .with("youtube-routing");
    }

}
