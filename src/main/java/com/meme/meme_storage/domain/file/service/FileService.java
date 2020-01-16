package com.meme.meme_storage.domain.file.service;

import com.meme.meme_storage.domain.file.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    public MemeFile findById(Long id) {
        return memeFileRepository.findById(id).get();
    }


    public Path pathSetting() throws IOException {
        Path path = Paths.get("resources/static/images").normalize();
        Files.createDirectories(path);
        return path;
    }

    public void saveTag(Tag tag) {
        tagRepository.save(tag);
    }

    public void saveMemeFileTag(MemeFileTag memeFileTag) {
        memeFileTagRepository.save(memeFileTag);
    }

    public List<MemeFile> findMemeFileByTag(String tagName){

        List<Tag> tags = tagRepository.findByTagName(tagName);

        return memeFileTagRepository.findByTag(tags);
    }
}
