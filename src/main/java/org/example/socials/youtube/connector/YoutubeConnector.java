package org.example.socials.youtube.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.socials.youtube.model.SocialItem;
import org.example.socials.youtube.model.YoutubeAccount;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YoutubeConnector {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate(); // TODO: to use bean creation using restTemplateBuilder?


    @SneakyThrows
    public void loadData(YoutubeAccount youtubeAccount) {
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {
        }).setApplicationName("youtube").build();
        List<SearchResult> searchResultList = youtube.search()
                .list("id,snippet")
                .setKey(youtubeAccount.getAccessToken())
                .setQ(youtubeAccount.getQuery() == null ? "" : youtubeAccount.getQuery())
                .setChannelId(youtubeAccount.getChannelId() == null ? "" : youtubeAccount.getChannelId())
                .setType("video") // See: https://developers.google.com/youtube/v3/docs/search/list#type
                .setVideoType("any") // Allowed values: [any, episode, movie]
                .setSafeSearch("none")
                .setOrder("relevance")
                .setFields("items(id/videoId,snippet/title,snippet/channelTitle,snippet/description,snippet/thumbnails/medium/url,snippet/thumbnails/high/url,snippet/channelId,snippet/publishedAt)")
                //.setForMine(true); // To increase efficiency, only retrieve the fields that the application uses.
                .setMaxResults(20L)
                .execute()
                .getItems();

        List<SocialItem> socialItems = transform(searchResultList, youtubeAccount);
        log.debug("Got {} items from youtube", socialItems.size());
        rabbitTemplate.convertAndSend("youtube-socials-storage",
                "youtube-routing",
                objectMapper.writeValueAsString(socialItems));

    }


    private List<SocialItem> transform(List<SearchResult> searchResultList, YoutubeAccount youtubeAccount) {
        List<SocialItem> youtube = searchResultList.stream().map(x -> new SocialItem(x.getId().getVideoId(),
                "YOUTUBE",
                youtubeAccount.getAccountName(),
                "https://www.youtube.com/watch?v=" + x.getId().getVideoId(),
                x.getSnippet().getTitle(),
                x.getSnippet().getDescription(),
                x.getSnippet().getThumbnails().getHigh().getUrl(),
                x.getSnippet().getThumbnails().getHigh().getUrl(),
                "https://www.youtube.com/watch?v=" + x.getId().getVideoId(),
                Instant.ofEpochMilli(x.getSnippet().getPublishedAt().getValue()),
                false
        )).collect(Collectors.toList());
        return youtube;
    }

}
