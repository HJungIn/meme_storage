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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
//
//    boolean memeFileSizeCheck = true;

    @RequestMapping("/uploadfile")
    public String fileSelect(Model model, @LoginUser SessionUser user) {

        if (user != null) {
            model.addAttribute("user", user);
        }

//        if (!memeFileSizeCheck)
//            model.addAttribute("error", "파일의 크기가 너무 큽니다");
//        memeFileSizeCheck = true;
        return "uploadfile";
    }


    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile,
                         @RequestParam("tagName") List<String> tagNames, Model model) throws IOException {

//            memeFileSizeCheck = fileService.memeFileSizeError(multipartFile.getSize());
//            if (!memeFileSizeCheck) {
//
//            }

            Path targetLocation = fileService.uploadPathSetting(multipartFile);

            MemeFile memeFile = new MemeFile(multipartFile.getOriginalFilename(), multipartFile.getContentType(), targetLocation.toString(), multipartFile.getSize());
            fileService.saveMemeFile(memeFile);


            for (String tagName : tagNames) {
                if (tagName.equals("")) continue;
                Tag tag = new Tag(tagName);
                fileService.saveTag(tag);

                fileService.saveMemeFileTag(MemeFileTag.createMemeFileTag(memeFile, tag));
            }

        return "redirect:/";
    }


//    @ExceptionHandler(Exception.class) //에러 페이지 하기
//    public String handleSizeExceededException(Exception e){
//        System.out.println("maxEx = "+ e.toString());
//        memeFileSizeCheck = false;
//        return "redirect:/uploadfile";
//    }

    /**
     * files
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
    public String tagSearch(@RequestParam("tagName") String tagNames, Model model, @LoginUser SessionUser user,
                            HttpServletRequest request,
                            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, size = 2) Pageable pageable) {

        if(tagNames.equals("")){
            return "redirect:/";
        }

        if (user != null) {
            model.addAttribute("user", user);
        }

        Page<MemeFile> files = fileService.getMemeFileByTags(tagNames, pageable);
        model.addAttribute("files", files);

        List<Integer> pages = fileService.getListPages(files.getTotalPages());
        model.addAttribute("pages", pages);
        model.addAttribute("searchCheck", true);

        return "home";
    }


    @GetMapping("/files/{id}")
    public String detailFile(@PathVariable Long id, Model model, @LoginUser SessionUser user) {

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
