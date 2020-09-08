package com.meme.meme_storage.domain.file.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class MemeFileTag {

    @Id
    @GeneratedValue
    @Column(name = "memefile_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memefile_id")
    private MemeFile memefile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    //==생성 메서드
    public static MemeFileTag createMemeFileTag(MemeFile memefile, Tag tag){
        MemeFileTag memeFileTag = new MemeFileTag();
        memeFileTag.setMemeFile(memefile);
        memeFileTag.setTag(tag);

        return memeFileTag;
    }

    //==연관관계 메서드==
    public void setMemeFile(MemeFile memefile){
        this.memefile = memefile;
        memefile.getMemeFileTags().add(this);
    }

    public void setTag(Tag tag){
        this.tag = tag;
        tag.getMemeFileTags().add(this);
    }

}
