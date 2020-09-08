package com.meme.meme_storage.controller;

import com.meme.meme_storage.config.oauth.LoginUser;
import com.meme.meme_storage.config.oauth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/loginPage")
    public String login(){


        return "login";
    }
}
