package org.example.socials.youtube.repository;


import org.example.socials.youtube.model.YoutubeAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<YoutubeAccount, String> {

    List<YoutubeAccount> findYoutubeAccountByExecutorScheduler(String message);

}
