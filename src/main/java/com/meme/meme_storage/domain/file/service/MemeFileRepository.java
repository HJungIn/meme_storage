package com.meme.meme_storage.domain.file.service;

import com.meme.meme_storage.domain.file.entity.MemeFile;
import com.meme.meme_storage.domain.file.entity.MemeFileTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemeFileRepository extends JpaRepository<MemeFile, Long>{

    List<MemeFile> findAll();

}
