package com.Linkdin.linkdinbackend.features.feed.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostDto {
    private String content;
    private String picture = null;

    public PostDto() {
    }

}

