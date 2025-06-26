package com.Linkdin.linkdinbackend.features.feed.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentDto {
    private String content;

    public CommentDto(String content) {
        this.content = content;
    }

    public CommentDto() {
    }

}

