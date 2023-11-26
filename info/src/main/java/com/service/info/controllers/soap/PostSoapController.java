package com.service.info.controllers.soap;

import com.service.info.domain.Post;
import com.service.info.service.PostService;
import io.info.post.GetPostRequest;
import io.info.post.GetPostResponse;
import io.info.post.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class PostSoapController {

    private static final Logger log = LoggerFactory.getLogger(PostSoapController.class);

    private final PostService postService;

    public PostSoapController(PostService postService) {
        this.postService = postService;
    }

    @PayloadRoot(namespace = "http://info.io/post", localPart = "getPostRequest")
    @ResponsePayload
    public GetPostResponse getPost(@RequestPayload GetPostRequest request) {
        log.info("soap request to get post with id {}", request.getId());
        Post post = postService.getPost(request.getId());

        GetPostResponse response = new ObjectFactory().createGetPostResponse();

        //todo figure out something with mapping
        io.info.post.Post xmlPost = new io.info.post.Post();
        xmlPost.setId(post.getId());
        xmlPost.setAuthor(post.getAuthor());
        xmlPost.setCategory(post.getCategory());
        xmlPost.setTitle(post.getTitle());

        response.setPost(xmlPost);
        return response;
    }


//todo implement all remaining methods
//    @PayloadRoot(namespace = "http://yournamespace.org", localPart = "GetPostsRequest")
//    @ResponsePayload
//    public GetPostsResponse getPosts(@RequestPayload GetPostsRequest request) {
//        log.info("soap request to get all posts");
//        List<Post> posts = postService.getAll();
//
//        GetPostsResponse response = new ObjectFactory().createGetPostsResponse();
//        response.getPosts().addAll(posts);
//
//        return response;
//    }

//    @PayloadRoot(namespace = "http://yournamespace.org", localPart = "AddPostRequest")
//    @ResponsePayload
//    public AddPostResponse addPost(@RequestPayload AddPostRequest request) {
//        log.info("soap request to save add post {}", request.getPost());
//        Post post = request.getPost();
//        post.setId(null);
//
//        Post insert = postService.insert(post);
//
//        AddPostResponse response = new ObjectFactory().createAddPostResponse();
//        response.setPost(insert);
//
//        return response;
//    }
//
//    @PayloadRoot(namespace = "http://yournamespace.org", localPart = "UpdatePostRequest")
//    @ResponsePayload
//    public UpdatePostResponse updatePost(@RequestPayload UpdatePostRequest request) {
//        log.info("soap request to update post {} with id {}", request.getPost(), request.getId());
//        Post post = request.getPost();
//        post.setId(request.getId());
//
//        Post update = postService.insert(post);
//
//        UpdatePostResponse response = new ObjectFactory().createUpdatePostResponse();
//        response.setPost(update);
//
//        return response;
//    }
}