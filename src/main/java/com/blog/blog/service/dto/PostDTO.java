package com.blog.blog.service.dto;

import com.blog.blog.domain.PostStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public class PostDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    private ZonedDateTime createDate;

    private PostStatus status;

    private ZonedDateTime publishDate;

}
