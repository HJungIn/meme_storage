package com.meme.meme_storage.controller;

import com.meme.meme_storage.config.oauth.LoginUser;
import com.meme.meme_storage.config.oauth.dto.SessionUser;
import com.meme.meme_storage.domain.file.entity.MemeFile;
import com.meme.meme_storage.domain.file.entity.MemeFileTag;
import com.meme.meme_storage.domain.file.entity.Tag;
import com.meme.meme_storage.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    boolean overlapCheck = false;

    @RequestMapping("/uploadfile")
    public String fileSelect(Model model, @LoginUser SessionUser user) {

        if (user != null) {
            model.addAttribute("user", user);
        }

        if(overlapCheck)
            model.addAttribute("error", "같은 이름의 파일이 존재합니다.");
        overlapCheck = false;
        return "uploadfile";
    }


    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile,
                         @RequestParam("tagName") List<String> tagNames, Model model) throws IOException {

        overlapCheck = fileService.nameOverlapCheck(multipartFile.getOriginalFilename());
        if(overlapCheck){
            return "redirect:/uploadfile";
        }

        Path targetLocation = fileService.uploadPathSetting(multipartFile);

        MemeFile memeFile = new MemeFile(multipartFile.getOriginalFilename(), multipartFile.getContentType(), targetLocation.toString(), multipartFile.getSize());
        fileService.saveMemeFile(memeFile);


        for (String tagName : tagNames) {
            if(tagName.equals("")) continue;
            Tag tag = new Tag(tagName);
            fileService.saveTag(tag);

            fileService.saveMemeFileTag(MemeFileTag.createMemeFileTag(memeFile, tag));
        }

        return "redirect:/";
    }

    /**
     *  files
     */

    @PostMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("fileId") Long id) throws IOException {

        MemeFile file = fileService.getMemeFileById(id);
        Resource resource = fileService.downPathSetting(file);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }

    /**
     * 검색
     */

    @GetMapping("/search")
    public String tagSearch(@RequestParam("tagName") String tagName, Model model, @LoginUser SessionUser user) {

        if (user != null) {
            model.addAttribute("user", user);
        }

        List<MemeFile> files = fileService.findMemeFileByTag(tagName);
        model.addAttribute("files", files);

        return "home";
    }


    @GetMapping("/files/{id}")
    public String detailFile(@PathVariable Long id, Model model, @LoginUser SessionUser user){

        MemeFile memeFile = fileService.getMemeFileById(id);
        model.addAttribute("memefile", memeFile);

        List<Tag> memeFileTags = fileService.getTagByMemeFile(memeFile);
        model.addAttribute("memefileTags", memeFileTags);


        if (user != null) {
            model.addAttribute("user", user);
        }

        return "detailfile";
    }
}
