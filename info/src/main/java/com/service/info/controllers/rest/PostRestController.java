package com.service.info.controllers.rest;

import com.service.info.domain.Post;
import com.service.info.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class PostRestController {

    private static final Logger log = LoggerFactory.getLogger(PostRestController.class);

    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getPost(@PathVariable String id) {
        log.info("rest request to get post with id {}", id);
        Post post = postService.getPost(id);
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(post);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts() {
        log.info("rest request to get post with id all posts");
        List<Post> post = postService.getAll();
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(post);
    }

    @PostMapping("/post")
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        log.info("rest request to save add post {}", post);
        post.setId(null);
        Post insert = postService.insert(post);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(insert);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<Post> updatePost(@RequestBody Post post, @PathVariable String id) {
        log.info("rest request to update post {} with id {}", post, id);
        post.setId(id);
        Post insert = postService.insert(post);
        return ResponseEntity.status(HttpStatus.OK)
                .body(insert);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        log.info("rest request to delete post with id {}", id);
        postService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
