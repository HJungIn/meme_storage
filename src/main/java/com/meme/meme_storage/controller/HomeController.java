package com.meme.meme_storage.controller;


import com.meme.meme_storage.config.oauth.LoginUser;
import com.meme.meme_storage.config.oauth.dto.SessionUser;
import com.meme.meme_storage.domain.account.User;
import com.meme.meme_storage.domain.file.entity.MemeFile;
import com.meme.meme_storage.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class HomeController {

    private final FileService fileService;

    @RequestMapping("/")
    public String homeAndFileList(Model model, @LoginUser SessionUser user) {

        List<MemeFile> files = fileService.findAllFiles();
        model.addAttribute("files", files);

        if (user != null) {

            model.addAttribute("user", user);
            System.out.println("user.getName() = " + user.getName());
        }

        return "home";
    }



}
