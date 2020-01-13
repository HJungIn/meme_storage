package com.meme.meme_storage.domain.file.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Data
public class File {

    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    private String fileName;
    private String fileType;
    private String fileDownloadUri;
    private long size;

    public File(){

    }

    public File(String fileName, String fileType, String fileDownloadUri, long size) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileDownloadUri = fileDownloadUri;
        this.size = size;
    }
}
