package org.example.socials.youtube.rabbit;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socials.youtube.service.YoutubeService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableRabbit
@AllArgsConstructor
public class YoutubeTokenListener {

    private final YoutubeService youtubeService;

    @RabbitListener(queues = "youtube-token-refresher")
    public void catchMessage(String message) {
        log.debug("catching token-refresh message: {}", message);
        youtubeService.refreshAccessToken(message);
    }

}
