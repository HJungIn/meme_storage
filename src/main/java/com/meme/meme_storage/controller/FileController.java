package com.meme.meme_storage.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @RequestMapping("/file")
    public String fileSelect() {
        return "file";
    }


    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile,
                         @RequestParam("tagName") List<String> tagNames) throws IOException {


        Path targetLocation = fileService.uploadPathSetting(multipartFile);

        MemeFile memeFile = new MemeFile(multipartFile.getOriginalFilename(), multipartFile.getContentType(), targetLocation.toString(), multipartFile.getSize());
        fileService.saveMemeFile(memeFile);


        for (String tagName : tagNames) {
            Tag tag = new Tag(tagName);
            fileService.saveTag(tag);

            fileService.saveMemeFileTag(MemeFileTag.createMemeFileTag(memeFile, tag));
        }

        return "redirect:/";
    }

    @GetMapping("/files")
    public String fileList(Model model) {
        List<MemeFile> files = fileService.findAllFiles();
        model.addAttribute("files", files);
        return "files";
    }

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
    public String tagSearch(@RequestParam("tagName") String tagName, Model model) {

        List<MemeFile> files = fileService.findMemeFileByTag(tagName);
        model.addAttribute("files", files);

        return "files";
    }

}
