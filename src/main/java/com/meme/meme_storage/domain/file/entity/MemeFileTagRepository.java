package com.meme.meme_storage.domain.file.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemeFileTagRepository extends JpaRepository<MemeFileTag, Long> {


    @Query("select mf.memefile from MemeFileTag mf where mf.tag in :tags")
    Page<MemeFile> findByTag(@Param("tags") List<Tag> tags, Pageable pageable);

    @Query("select mf.tag from MemeFileTag mf where mf.memefile = :memeFile")
    List<Tag> findTagsByMemeFile(@Param("memeFile") MemeFile memeFile);

    @Query("select mf.id from MemeFileTag mf where mf.memefile = :memefile and mf.tag = :tag ")
    Long findMemeFileTagIdByMemeFileAndTag(@Param("memefile") Optional<MemeFile> memefile, @Param("tag")Optional<Tag> tag);
}
