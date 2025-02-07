package com.waqas.social.media.platform.controller;

import com.waqas.social.media.platform.domain.Post;
import com.waqas.social.media.platform.request.CommentRequest;
import com.waqas.social.media.platform.request.PostRequest;
import com.waqas.social.media.platform.service.PostService;
import com.waqas.social.media.platform.utils.HttpRequestResult;
import com.waqas.social.media.platform.utils.SortType;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private static final Logger application = LoggerFactory.getLogger(UserController.class);

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "create new post")
    @PostMapping
    public ResponseEntity<HttpRequestResult> createPost(@RequestBody PostRequest postRequest) {
        application.info("calling create /posts");
        return postService.createPost(postRequest);
    }

    @Operation(summary = "get all posts")
    @GetMapping
    public ResponseEntity<HttpRequestResult> getAllPosts(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam SortType sortType) {
        application.info("Get All Posts");
        return postService.getAllPosts(pageNumber, pageSize, sortType);
    }

    @Operation(summary = "get post by id")
    @GetMapping("/{id}")
    public ResponseEntity<HttpRequestResult> getPostById(@PathVariable Long id) {
        application.info("Get Post By Id {}", id);
        return postService.getPostById(id);
    }

    @Operation(summary = "update post by id")
    @PutMapping("/{id}")
    public ResponseEntity<HttpRequestResult> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest) {
        application.info("Update Post By Id {}", id);
        return postService.updatePostById(id, postRequest);
    }

    @Operation(summary = "delete post by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpRequestResult> deletePost(@PathVariable Long id) {
        application.info("Delete Post By Id {}", id);
        return postService.deletePostById(id);
    }

    @Operation(summary = "comment a post by id")
    @PostMapping("/{id}/comments")
    public ResponseEntity<HttpRequestResult> addComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        application.info("calling /posts/{id}/comments");
        return postService.commentPost(id, commentRequest);
    }

    @Operation(summary = "like a post by id")
    @PostMapping("/{id}/like")
    public ResponseEntity<HttpRequestResult> likePost(@PathVariable Long id) {
        application.info("calling /posts/{id}/like");
        return postService.likePost(id);
    }

    @Operation(summary = "search post by keyword")
    @PostMapping("/search")
    public ResponseEntity<HttpRequestResult> searchPosts(@RequestParam String keyword, @RequestParam int pageNumber, @RequestParam int pageSize) {
        application.info("calling /posts/search");
        return postService.searchPosts(keyword, pageNumber, pageSize);
    }
}
