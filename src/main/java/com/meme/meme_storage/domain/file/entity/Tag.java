package com.meme.meme_storage.domain.file.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Tag {

    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    private String tagName;

    @OneToMany(mappedBy = "tag")
    private List<MemeFileTag> memeFileTags = new ArrayList<>();

    public Tag(String tagName) {
        this.tagName = tagName;
    }

}
