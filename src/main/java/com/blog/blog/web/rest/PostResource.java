package com.blog.blog.web.rest;

import com.blog.blog.service.PostService;
import com.blog.blog.service.dto.PostDTO;
import com.blog.blog.service.error.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PostResource {

    private final Logger log = LoggerFactory.getLogger(PostResource.class);

    private static final String ENTITY_NAME = "post";

    private PostService postService;

    public PostResource(PostService postService) {
        this.postService = postService;
    }

    /**
     * POST  /posts : Create a new post.
     *
     * @param postDTO the postDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new postDTO, or with status 400 (Bad Request) if the post has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/posts")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO) throws URISyntaxException {
        log.debug("REST request to save Post : {}", postDTO);
        if (postDTO.getId() != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "A new post cannot already have an ID");
        }
        PostDTO result = postService.save(postDTO);
        return ResponseEntity.created(new URI("/api/posts/" + result.getId()))
                .body(result);
    }

    /**
     * PUT  /posts : Updates an existing post.
     *
     * @param postDTO the postDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated postDTO,
     * or with status 400 (Bad Request) if the postDTO is not valid,
     * or with status 500 (Internal Server Error) if the postDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/posts")
    public ResponseEntity<PostDTO> updatePost(@Valid @RequestBody PostDTO postDTO) throws URISyntaxException {
        log.debug("REST request to update Post : {}", postDTO);
        if (postDTO.getId() == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        PostDTO result = postService.save(postDTO);
        return ResponseEntity.ok()
                .body(result);
    }

    /**
     * GET  /posts : get all the posts.
     *
     * @param pageable the page specification that requested
     * @return the ResponseEntity with status 200 (OK) and the list of posts in body
     */
    @GetMapping("/posts")
    public ResponseEntity<Page<PostDTO>> getAllPosts(Pageable pageable) {
        log.debug("REST request to get Posts by page: {}", pageable);
        Page<PostDTO> postPage = postService.findAllByPage(pageable);
        return ResponseEntity.ok().body(postPage);
    }

    /**
     * GET  /posts/:id : get the "id" post.
     *
     * @param id the id of the postDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the postDTO, or with status 404 (Not Found)
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
        log.debug("REST request to get Post : {}", id);
        Optional<PostDTO> postDTOOptional = postService.findOne(id);
        return ResponseEntity.ok().body(postDTOOptional.orElseThrow(NotFoundException::new));
    }

    /**
     * DELETE  /posts/:id : delete the "id" post.
     *
     * @param id the id of the postDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.debug("REST request to delete Post : {}", id);
        postService.delete(id);
        return ResponseEntity.ok().build();
    }

}
