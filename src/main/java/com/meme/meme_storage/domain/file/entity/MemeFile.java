package com.meme.meme_storage.domain.file.entity;

import com.meme.meme_storage.domain.BaseTimeEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class MemeFile extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    private String fileName;
    private String fileType;
    private String filePath;
    private long size;

    public MemeFile(){

    }

    public MemeFile(String fileName, String fileType, String filePath, long size) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.size = size;
    }
}
