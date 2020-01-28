package com.meme.meme_storage.controller;


import com.meme.meme_storage.config.oauth.LoginUser;
import com.meme.meme_storage.config.oauth.dto.SessionUser;
import com.meme.meme_storage.domain.account.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@RequiredArgsConstructor
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(Model model, @LoginUser SessionUser user) {


        if (user != null) {

            model.addAttribute("userName", user.getName());

        }

        return "home";
    }
}
