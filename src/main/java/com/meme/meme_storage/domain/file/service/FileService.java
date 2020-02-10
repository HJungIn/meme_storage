package com.meme.meme_storage.domain.file.service;

import com.meme.meme_storage.domain.file.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<MemeFile> findAllFiles(Pageable pageable) {
        return memeFileRepository.findAll(pageable);
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

    public Page<MemeFile> getMemeFileByTags(String tagNameStr, Pageable pageable) {

        String[] tagNames = tagNameStr.split(" ");
        List<Tag> tags = tagRepository.findByTagNames(tagNames);

        if (tags.size() == 0) {
            return Page.empty();
        }


        Page<MemeFile> memeFiles = memeFileTagRepository.findByTag(tags, pageable);
        return memeFiles;
    }


    public List<Tag> getTagByMemeFile(MemeFile memeFile) {

        List<MemeFileTag> memeFileTags = memeFile.getMemeFileTags();
        List<Tag> memeFileTagsString = new ArrayList<>();
        for (MemeFileTag memeFileTag : memeFileTags) {
            memeFileTagsString.add( memeFileTag.getTag() );
        }
        return memeFileTagsString;
    }

    public List<Integer> getListPages(int totalPages) {
        List<Integer> pages = new ArrayList<>();
        for(int i=0;i<totalPages;i++){
            pages.add(i+1);
        }
        return pages;
    }

}
