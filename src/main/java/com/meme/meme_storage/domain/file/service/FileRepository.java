package com.meme.meme_storage.domain.file.service;

import com.meme.meme_storage.domain.file.entity.MemeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<MemeFile, Long>{

    List<MemeFile> findAll();

}
