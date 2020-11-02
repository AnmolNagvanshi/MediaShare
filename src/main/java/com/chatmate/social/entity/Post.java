package com.chatmate.social.entity;

import javax.validation.constraints.NotBlank;

public class Post {

    private Long postId;

    @NotBlank
    private String body;

    private long timestamp;

    private Long userId;

}
