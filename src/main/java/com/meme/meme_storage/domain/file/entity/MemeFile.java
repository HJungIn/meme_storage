package com.meme.meme_storage.domain.file.entity;

import com.meme.meme_storage.domain.BaseTimeEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class MemeFile extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "memefile_id")
    private Long id;

    private String fileName;
    private String fileType;
    private String filePath;
    private long size;

    @OneToMany(mappedBy = "memefile", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemeFileTag> memeFileTags = new ArrayList<>();

    public MemeFile(){

    }

    public MemeFile(String fileName, String fileType, String filePath, long size) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.size = size;

    }
}
