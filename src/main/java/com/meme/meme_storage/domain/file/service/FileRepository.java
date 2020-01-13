package com.meme.meme_storage.domain.file.service;

import com.meme.meme_storage.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {


}
