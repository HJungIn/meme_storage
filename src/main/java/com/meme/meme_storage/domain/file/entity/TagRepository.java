package com.meme.meme_storage.domain.file.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select t from Tag t where t.tagName in :tagNames")
    List<Tag> findByTagNames(@Param("tagNames") String[] tagNames);

    @Query("select t from Tag t where t.tagName = :tagName")
    Tag findByTagName(@Param("tagName") String tagName);
}
