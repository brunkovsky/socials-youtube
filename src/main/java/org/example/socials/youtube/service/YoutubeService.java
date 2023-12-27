package org.example.socials.youtube.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socials.youtube.connector.YoutubeConnector;
import org.example.socials.youtube.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class YoutubeService {

    private final AccountRepository accountRepository;
    private final YoutubeConnector connector;


    public void fetch(String executorScheduler) {
        accountRepository.findYoutubeAccountByExecutorScheduler(executorScheduler)
                .forEach(connector::loadData);
    }

    public void refreshAccessToken(String accountName) {
        log.info("refreshing access token : " + accountName);
    }

}
