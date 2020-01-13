package com.meme.meme_storage.domain.file.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    private Path fileLocation;

    public FileService() {
        this.fileLocation = Paths.get("/Users/HUH/IdeaProjects/프로젝트/meme_storage/src/main/resources/static/images")
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileLocation);
        }catch(Exception e) {
            //throw new FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
        }
    }



    //save
    public String  saveFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try{
//            if(fileName.contains(".."))
//                throw new FileUploadException("부적합한 문자 포함됨"+fileName);

            Path targetLocation = this.fileLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e){

        }
        return null;
    }




}
