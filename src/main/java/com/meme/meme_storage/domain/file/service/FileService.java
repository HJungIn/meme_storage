package com.meme.meme_storage.domain.file.service;

import com.meme.meme_storage.domain.file.entity.MemeFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public void save(MemeFile memeFile) {
        fileRepository.save(memeFile);
    }

    public List<MemeFile> findAllFiles() {
        return fileRepository.findAll();
    }

    public MemeFile findById(Long id) {
        return fileRepository.findById(id).get();
    }


    public Path pathSetting() throws IOException {
        Path path = Paths.get("resources/static/images").normalize();
        Files.createDirectories(path);
        return path;
    }
}
