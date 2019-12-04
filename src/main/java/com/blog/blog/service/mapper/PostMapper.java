package com.blog.blog.service.mapper;

import com.blog.blog.domain.*;
import com.blog.blog.service.dto.PostDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PostMapper extends EntityMapper<PostDTO, Post> {
    PostDTO toDto(Post post);

    Post toEntity(PostDTO postDTO);

    default Post fromId(Long id) {
        if (id == null) {
            return null;
        }
        Post post = new Post();
        post.setId(id);
        return post;
    }
}
