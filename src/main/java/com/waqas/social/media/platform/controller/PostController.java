package com.waqas.social.media.platform.controller;

import com.waqas.social.media.platform.domain.Post;
import com.waqas.social.media.platform.domain.User;
import com.waqas.social.media.platform.domain.Comment;
import com.waqas.social.media.platform.domain.PostLike;
import com.waqas.social.media.platform.repository.PostRepository;
import com.waqas.social.media.platform.repository.CommentRepository;
import com.waqas.social.media.platform.repository.PostLikeRepository;
import com.waqas.social.media.platform.request.PostRequest;
import com.waqas.social.media.platform.service.PostService;
import com.waqas.social.media.platform.utils.HttpRequestResult;
import com.waqas.social.media.platform.utils.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<HttpRequestResult> createPost(@RequestBody PostRequest postRequest) {

        return postService.createPost(postRequest);
    }

    @GetMapping
    public ResponseEntity<HttpRequestResult> getAllPosts(@RequestParam int page, @RequestParam int size, @RequestParam SortType sortType) {
        // Implement logic to retrieve all posts with pagination and sorting
        return postService.getAllPosts(page, size, sortType);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpRequestResult> getPostById(@PathVariable Long id) {

        return postService.getPostById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpRequestResult> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest) {
        return postService.updatePostById(id, postRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpRequestResult> deletePost(@PathVariable Long id) {
        return postService.deletePostById(id);
    }

    @PostMapping("/{id}/comments")
    public Comment addComment(@PathVariable Long id, @RequestBody Comment comment) {
        Post post = postRepository.findById(id).orElseThrow();
//        comment.setPost(post);
        return commentRepository.save(comment);
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable Long id, @RequestBody User user) {
        Post post = postRepository.findById(id).orElseThrow();
        PostLike postLike = new PostLike();
//        postLike.setPost(post);
//        postLike.setUser(user);
        postLikeRepository.save(postLike);
        return "Post liked";
    }

    @PostMapping("/search")
    public List<Post> searchPosts(@RequestParam String keyword) {
        //return postRepository.findByContentContaining(keyword, keyword);
        return null;
    }
}
