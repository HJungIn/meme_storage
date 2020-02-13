package com.meme.meme_storage.controller;

import com.meme.meme_storage.config.oauth.LoginUser;
import com.meme.meme_storage.config.oauth.dto.SessionUser;
import com.meme.meme_storage.domain.file.entity.MemeFile;
import com.meme.meme_storage.domain.file.entity.Tag;
import com.meme.meme_storage.domain.file.service.FileService;
import com.meme.meme_storage.domain.file.service.UserService;
import com.sangupta.murmur.Murmur3;
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

    @RequestMapping("/uploadfile")
    public String fileSelect(Model model, @LoginUser SessionUser user) {

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("userRole",userService.getUserRole(user.getEmail()).equals("ROLE_ADMIN"));
        }

        return "uploadfile";
    }

    private static final long SEED = 0x7f3a21eal;
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile,
                         @RequestParam("tagName") String tagNameStr, Model model) throws IOException {


        long hashFilename = Murmur3.hash_x86_32(multipartFile.getOriginalFilename().getBytes(), multipartFile.getOriginalFilename().length(), SEED);
        String hashFilenameStr = String.valueOf(hashFilename);

        String[] pointSplit = multipartFile.getOriginalFilename().split("\\.");
        String typeName = pointSplit[pointSplit.length-1];
        String hashFilenameAndTypename = hashFilenameStr + "." + typeName;

        Path targetLocation = fileService.uploadPathSetting(multipartFile, hashFilenameAndTypename);

        MemeFile memeFile = new MemeFile(hashFilenameAndTypename, multipartFile.getContentType(), targetLocation.toString(), multipartFile.getSize());
            fileService.saveMemeFile(memeFile);


            fileService.saveTagAndMemeFileTagByTagName(tagNameStr, memeFile, false);

        return "redirect:/";
    }

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
    private final UserService userService;

    @GetMapping("/search")
    public String tagSearch(@RequestParam("tagName") String tagNames, Model model, @LoginUser SessionUser user,
                            HttpServletRequest request,
                            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, size = 2) Pageable pageable) {

        if(tagNames.equals("")){
            return "redirect:/";
        }

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("userRole",userService.getUserRole(user.getEmail()).equals("ROLE_ADMIN"));
        }

        Page<MemeFile> files = fileService.getMemeFileByTags(tagNames, pageable);
        model.addAttribute("files", files);
        model.addAttribute("currentPage", files.getNumber());
        model.addAttribute("lastPage", files.getTotalPages());

        int tenCheckPage = 1;
        tenCheckPage += files.getNumber()/10;

        List<Integer> pages = fileService.getListPages(tenCheckPage, files.getTotalPages());
        model.addAttribute("pages", pages);
        model.addAttribute("searchCheck", true);

        return "home";
    }


    @GetMapping("/files/{id}")
    public String detailFile(@PathVariable Long id, Model model, @LoginUser SessionUser user) throws Exception{

        MemeFile memeFile = fileService.getMemeFileById(id);
        model.addAttribute("memefile", memeFile);

        List<Tag> memeFileTags = fileService.getTagByMemeFile(memeFile);
        model.addAttribute("memefileTags", memeFileTags);


        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("userRole",userService.getUserRole(user.getEmail()).equals("ROLE_ADMIN"));
        }

        return "detailfile";
    }

    @PostMapping("/detailFileAddTag")
    public String detailFileAddTag(@RequestParam("memefileId") Long id, @RequestParam("tagName") String tagNameStr){

        MemeFile memeFile = fileService.getMemeFileById(id);

        fileService.saveTagAndMemeFileTagByTagName(tagNameStr, memeFile, true);

        return "redirect:/files/"+id;
    }


}
