package com.chatmate.social.entity;

import javax.validation.constraints.NotBlank;

public class Post {

    private Integer postId;

    @NotBlank
    private String body;

    private long timestamp;

    private Integer userId;

}
