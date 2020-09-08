package com.meme.meme_storage.controller;


import com.meme.meme_storage.config.oauth.LoginUser;
import com.meme.meme_storage.config.oauth.dto.SessionUser;
import com.meme.meme_storage.domain.account.User;
import com.meme.meme_storage.domain.file.entity.MemeFile;
import com.meme.meme_storage.domain.file.service.FileService;
import com.meme.meme_storage.domain.file.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class HomeController {

    private final FileService fileService;
    private final UserService userService;

    @RequestMapping(value = {"/", "/gallery"})
    public String homeAndFileList(Model model, @LoginUser SessionUser user,
                                  @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 2) Pageable pageable) {

        Page<MemeFile> files = fileService.findAllFiles(pageable);
        model.addAttribute("files", files);
        model.addAttribute("currentPage", files.getNumber());
        model.addAttribute("lastPage", files.getTotalPages());

        int tenCheckPage = 1;
        tenCheckPage += files.getNumber()/10;



        List<Integer> pages = fileService.getListPages(tenCheckPage, files.getTotalPages());
        model.addAttribute("pages", pages);
        model.addAttribute("searchCheck", false);

        if (user != null) {
            model.addAttribute("user", user);
            System.out.println("user "+userService.getUserRole(user.getEmail()));
            model.addAttribute("userRole",userService.getUserRole(user.getEmail()).equals("ROLE_ADMIN"));
        }


        return "home";
    }


}
