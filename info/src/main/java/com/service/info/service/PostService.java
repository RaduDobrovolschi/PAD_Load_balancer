package com.service.info.service;

import com.service.info.domain.Post;
import com.service.info.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post getPost(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Post insert(Post post){
        return postRepository.save(post);
    }

    public void delete(String id) {
        postRepository.deleteById(id);
    }
}
