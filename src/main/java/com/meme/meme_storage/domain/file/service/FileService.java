package com.meme.meme_storage.domain.file.service;

import com.meme.meme_storage.domain.file.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final MemeFileRepository memeFileRepository;
    private final TagRepository tagRepository;
    private final MemeFileTagRepository memeFileTagRepository;

    public void saveMemeFile(MemeFile memeFile) {
        memeFileRepository.save(memeFile);
    }

    public List<MemeFile> findAllFiles() {
        return memeFileRepository.findAll();
    }

    public MemeFile getMemeFileById(Long id) {
        Optional<MemeFile> memeFiles = memeFileRepository.findById(id);
        if (!memeFiles.isPresent()) {
            return new MemeFile("", "", "", 0);
        }

        return memeFileRepository.findById(id).get();
    }


    public Path uploadPathSetting(MultipartFile multipartFile) throws IOException {
        Path fileLocation = Paths.get("src/main/upload/static/images").normalize();
        Path targetLocation = fileLocation.resolve(multipartFile.getOriginalFilename());
        Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return targetLocation;
    }

    public Resource downPathSetting(MemeFile file) throws MalformedURLException {
        Path fileLocation = Paths.get("src/main/upload/static/images").normalize();
        Path filePath = fileLocation.resolve(file.getFileName()).normalize();
        Resource Resource = new UrlResource(filePath.toUri());
        return Resource;
    }

    public void saveTag(Tag tag) {
        tagRepository.save(tag);
    }

    public void saveMemeFileTag(MemeFileTag memeFileTag) {
        memeFileTagRepository.save(memeFileTag);
    }

    public List<MemeFile> findMemeFileByTag(String tagName) {
        List<Tag> tags = tagRepository.findByTagName(tagName);

        if (tags.size() == 0) {
            List<MemeFile> emptyFiles = new ArrayList<>();
            return emptyFiles;
        }

        List<MemeFile> memeFiles = memeFileTagRepository.findByTag(tags);


        return memeFiles;
    }


}
