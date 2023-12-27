package org.example.socials.youtube.controller;

import lombok.AllArgsConstructor;
import org.example.socials.youtube.model.YoutubeAccount;
import org.example.socials.youtube.repository.AccountRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "api/socials/youtube")
@AllArgsConstructor
public class UiController {

    private final AccountRepository accountRepository;

    @GetMapping("/ui")
    public String search(Model model) {
        model.addAttribute("youtubeAccounts", accountRepository.findAll());
        return "accounts";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable("id") String id) {
        accountRepository.deleteById(id);
        return "redirect:/api/socials/youtube/ui";
    }

    @PostMapping("/addAccount")
    public String addAccount(YoutubeAccount youtubeAccount) {
        accountRepository.save(youtubeAccount);
        return "redirect:/api/socials/youtube/ui";
    }

}
