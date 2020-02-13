package com.meme.meme_storage.controller;

import com.meme.meme_storage.config.oauth.LoginUser;
import com.meme.meme_storage.config.oauth.dto.SessionUser;
import com.meme.meme_storage.domain.file.entity.MemeFile;
import com.meme.meme_storage.domain.file.service.FileService;
import com.meme.meme_storage.domain.file.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final FileService fileService;

    @GetMapping("/admin/removeMemeFile/{id}")
    public String removeMemeFile(@PathVariable("id") Long memefileId){

        fileService.removeMemeFile(memefileId);
        return "redirect:/";
    }



    @GetMapping("/admin/removeMemeFileTag/{memefileid}/{tagid}")
    public String removeMemeFileTag(@PathVariable("memefileid") Long memefileId, @PathVariable("tagid") Long tagId){

        fileService.removeMemeFileTag(memefileId, tagId);

        System.out.println("memefileId = " + memefileId);
        System.out.println("whoisid = " + tagId);
        return "redirect:/";
    }
}
