package com.meme.meme_storage.controller;


import com.meme.meme_storage.config.oauth.LoginUser;
import com.meme.meme_storage.config.oauth.dto.SessionUser;
import com.meme.meme_storage.domain.account.User;
import com.meme.meme_storage.domain.file.entity.MemeFile;
import com.meme.meme_storage.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public String homeAndFileList(Model model, @LoginUser SessionUser user,
                                  @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, size = 12) Pageable pageable) {

        Page<MemeFile> files = fileService.findAllFiles(pageable);
        model.addAttribute("files", files);

        model.addAttribute("pageable", true);
        model.addAttribute("previous", files.getNumber()-1);
        model.addAttribute("next", files.getNumber()+1);
        model.addAttribute("lastpage", files.getTotalPages()-1);

        if (user != null) {
            model.addAttribute("user", user);
        }

        return "home";
    }



}
