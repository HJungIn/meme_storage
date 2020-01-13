package com.meme.meme_storage.controller;

import com.meme.meme_storage.domain.file.entity.File;
import com.meme.meme_storage.domain.file.service.FileRepository;
import com.meme.meme_storage.domain.file.service.FileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller

public class FileController {

    private FileService fileService;
    private FileRepository fileRepository;

    public FileController(FileService fileService, FileRepository fileRepository) {
        this.fileService = fileService;
        this.fileRepository = fileRepository;
    }

    private String finalFileName;

    @RequestMapping("/file")
    public String fileSelect(Model model){
        System.out.println("file");
        model.addAttribute("fileName", finalFileName);

        return "file";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file")MultipartFile multipartFile){

        System.out.println(multipartFile.getOriginalFilename());
        System.out.println("multipartFile.getContentType() = " + multipartFile.getContentType());
        System.out.println("multipartFile.getSize() = " + multipartFile.getSize());

        String fileName = fileService.saveFile(multipartFile);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(fileName)
                .toUriString();

        System.out.println("fileDownloadUri = " + fileDownloadUri);
        File file = new File(multipartFile.getOriginalFilename(), multipartFile.getContentType(), fileDownloadUri,multipartFile.getSize());
        fileRepository.save(file);

        //확인용
        finalFileName = fileName;

        return "redirect:/file";
    }



}
