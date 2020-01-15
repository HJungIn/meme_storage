package com.meme.meme_storage.controller;

import com.meme.meme_storage.domain.file.entity.MemeFile;
import com.meme.meme_storage.domain.file.service.FileRepository;
import com.meme.meme_storage.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @RequestMapping("/file")
    public String fileSelect(){
        return "file";
    }


    @PostMapping("/upload")
    public String upload( HttpServletRequest request,
                         @RequestParam("file")MultipartFile multipartFile) throws IOException{

        Path fileLocation = fileService.pathSetting();
        Path targetLocation = fileLocation.resolve(multipartFile.getOriginalFilename());
        Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        MemeFile memeFile = new MemeFile(multipartFile.getOriginalFilename(), multipartFile.getContentType(), targetLocation.toString(), multipartFile.getSize());
        fileService.save(memeFile);

        return "redirect:/";
    }

    @GetMapping("/files")
    public String fileList(Model model){
        List<MemeFile> files= fileService.findAllFiles();
        model.addAttribute("files", files);
        return "files";
    }

    @PostMapping("/download")
    public ResponseEntity<Resource> download( @RequestParam("fileId") Long id) throws IOException{

        MemeFile file = fileService.findById(id);
        Path fileLocation = fileService.pathSetting();
        Path filePath = fileLocation.resolve(file.getFileName()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }

}
