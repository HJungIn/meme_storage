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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

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


    public Path uploadPathSetting(MultipartFile multipartFile, String hashFilenameStr) throws IOException {
        Path fileLocation = Paths.get("src/main/upload/static/images").normalize();
        Path targetLocation = fileLocation.resolve(hashFilenameStr);

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
            memeFileTagsString.add(memeFileTag.getTag());
        }
        return memeFileTagsString;
    }

    public List<Integer> getListPages(int tenCheckPage, int totalPages) {
        List<Integer> pages = new ArrayList<>();

        if (totalPages < tenCheckPage * 10) {
            for (int i = (tenCheckPage - 1) * 10 + 1; i <= totalPages; i++) {
                pages.add(i);
            }
            return pages;
        } else {
            for (int i = (tenCheckPage - 1) * 10 + 1; i <= tenCheckPage * 10; i++) {
                pages.add(i);
            }
            return pages;
        }

    }


    public void saveTagAndMemeFileTagByTagName(String tagNameStr, MemeFile memeFile, boolean findTagsByMemeFileCheck) {

        List<Tag> tagsByMemeFile = new ArrayList<>();
        if (findTagsByMemeFileCheck) {
            tagsByMemeFile = memeFileTagRepository.findTagsByMemeFile(memeFile);
        }

        String[] tagNames = tagNameStr.split(",");
        for (String tagName : tagNames) {
            tagName = tagName.replaceAll(" ", "");
            if (tagName.equals("")) continue;

            Tag tagByTagName = tagRepository.findByTagName(tagName);
            if (tagsByMemeFile.contains(tagByTagName)) continue;

            if (tagByTagName != null) {
                saveMemeFileTag(MemeFileTag.createMemeFileTag(memeFile, tagByTagName));
                tagsByMemeFile.add(tagByTagName);
                continue;
            }

            Tag tag = new Tag(tagName);
            saveTag(tag);
            tagsByMemeFile.add(tag);

            saveMemeFileTag(MemeFileTag.createMemeFileTag(memeFile, tag));
        }
    }

    public void removeMemeFile(Long id) {
        memeFileRepository.deleteById(id);
    }

    public void removeMemeFileTag(Long memefileId, Long tagId) {

        Optional<MemeFile> memeFileById = memeFileRepository.findById(memefileId);
        Optional<Tag> tagById = tagRepository.findById(tagId);

        Long memeFileTagIdByMemeFileAndTag = memeFileTagRepository.findMemeFileTagIdByMemeFileAndTag(memeFileById, tagById);
        System.out.println("memeFileTagIdByMemeFileIdAndTagId = " + memeFileTagIdByMemeFileAndTag);

        memeFileTagRepository.deleteById(memeFileTagIdByMemeFileAndTag);
    }

}
