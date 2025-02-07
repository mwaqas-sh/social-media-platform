package com.waqas.social.media.platform.service;

import com.waqas.social.media.platform.domain.Comment;
import com.waqas.social.media.platform.domain.Post;
import com.waqas.social.media.platform.domain.PostLike;
import com.waqas.social.media.platform.domain.User;
import com.waqas.social.media.platform.repository.CommentRepository;
import com.waqas.social.media.platform.repository.PostLikeRepository;
import com.waqas.social.media.platform.repository.PostRepository;
import com.waqas.social.media.platform.repository.UserRepository;
import com.waqas.social.media.platform.request.CommentRequest;
import com.waqas.social.media.platform.request.PostRequest;
import com.waqas.social.media.platform.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.waqas.social.media.platform.utils.Constants.SUCCESS;

@Service
public class PostService {

    private static final Logger application = LoggerFactory.getLogger(UserService.class);

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Value(value = "${jwt.secret}")
    String jwtSecret;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, PostLikeRepository postLikeRepository, UserRepository userRepository, UserService userService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.postLikeRepository = postLikeRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public ResponseEntity<HttpRequestResult> createPost(PostRequest postRequest) {
        long userIdFromToken = JwtHelper.getUserIdByToken(jwtSecret);
        long currentDate = Instant.now().toEpochMilli();
        Post post = new Post();
        post.setCreatedDate(currentDate);
        post.setLastModifiedDate(currentDate);
        post.setContent(postRequest.getContent());
        User user = userRepository.findByUserId(userIdFromToken);
        if (user == null) {
            return Utilities.sendHttpBadRequestResponse(Constants.USER_NOT_FOUND,null,"");
        }
        post.setUser(user);
        postRepository.save(post);
        return Utilities.sendHttpSuccessResponse(SUCCESS, post, "");
    }

    public ResponseEntity<HttpRequestResult> getAllPosts(int pageNumber, int pageSize, SortType sortOrder) {
        Sort sort = sortOrder == SortType.DESC ? Sort.by("createdDate").descending() : Sort.by("createdDate").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);
        if(posts.isEmpty()) {
            application.info("No posts found");
            return Utilities.sendHttpSuccessResponse(Constants.NO_RECORD_FOUND, posts, "");
        } else {
            application.info("All users fetched successfully");
            return Utilities.sendHttpSuccessResponse(SUCCESS, posts, "");
        }
    }

    public ResponseEntity<HttpRequestResult> getPostById(Long id) {
        Post post = postRepository.getById(id);
        if (post != null) {
            application.info(" Get User Detail successfully");
            return Utilities.sendHttpSuccessResponse(SUCCESS, post, "");
        }
        return Utilities.sendHttpBadRequestResponse(Constants.USER_NOT_FOUND, null, "");
    }

    public ResponseEntity<HttpRequestResult> updatePostById(Long id, PostRequest postRequest) {
        final long currentDate = Instant.now().toEpochMilli();
        long userIdFromToken = JwtHelper.getUserIdByToken(jwtSecret);
        Post post = postRepository.getById(id);
        if(post == null) {
            return Utilities.sendHttpBadRequestResponse(Constants.NO_RECORD_FOUND, null, "");
        }
        if(userIdFromToken != post.getUser().getId()) {
            return Utilities.sendHttpBadRequestResponse(Constants.USER_NOT_VERIFIED, null, "");
        }

        post.setLastModifiedDate(currentDate);
        post.setContent(postRequest.getContent());
        postRepository.save(post);
        return Utilities.sendHttpSuccessResponse(Constants.SUCCESS, post, "");
    }

    public ResponseEntity<HttpRequestResult> deletePostById(Long id) {
        long userIdFromToken = JwtHelper.getUserIdByToken(jwtSecret);
        Post post = postRepository.getById(id);
        if(post == null) {
            return Utilities.sendHttpBadRequestResponse(Constants.NO_RECORD_FOUND, null, "");
        }
        if(userIdFromToken != post.getUser().getId()) {
            return Utilities.sendHttpBadRequestResponse(Constants.USER_NOT_VERIFIED, null, "");
        }

        postRepository.delete(post);
        return Utilities.sendHttpSuccessResponse(Constants.SUCCESS, "Deleted Successfully", "");
    }

    public ResponseEntity<HttpRequestResult> commentPost(Long id, CommentRequest commentRequest) {
        final long currentDate = Instant.now().toEpochMilli();
        Post post = postRepository.getById(id);
        if(post == null) {
            return Utilities.sendHttpBadRequestResponse(Constants.NO_RECORD_FOUND, null, "");
        }
        long userIdFromToken = JwtHelper.getUserIdByToken(jwtSecret);
        User user = userRepository.findById(userIdFromToken).get();
        if(user == null) {
            return Utilities.sendHttpBadRequestResponse(Constants.USER_NOT_FOUND, null, "");
        }
        Comment comment = new Comment();
        comment.setCreatedDate(currentDate);
        comment.setLastModifiedDate(currentDate);
        comment.setContent(commentRequest.getContent());
        comment.setUser(userRepository.findById(userIdFromToken).get());
        comment.setPost(post);
        commentRepository.save(comment);
        return Utilities.sendHttpSuccessResponse(Constants.SUCCESS, comment, "");
    }

    public ResponseEntity<HttpRequestResult> likePost(Long id) {
        final long currentDate = Instant.now().toEpochMilli();
        Post post = postRepository.getById(id);
        if(post == null) {
            return Utilities.sendHttpBadRequestResponse(Constants.NO_RECORD_FOUND, null, "");
        }
        long userIdFromToken = JwtHelper.getUserIdByToken(jwtSecret);
        User user = userRepository.findById(userIdFromToken).get();
        if(user == null) {
            return Utilities.sendHttpBadRequestResponse(Constants.USER_NOT_FOUND, null, "");
        }
        PostLike postLike = new PostLike();
        postLike.setPost(post);
        postLike.setUser(user);
        postLike.setCreatedDate(currentDate);
        postLike.setLastModifiedDate(currentDate);
        postLikeRepository.save(postLike);
        return Utilities.sendHttpSuccessResponse(Constants.SUCCESS, postLike, "");
    }
}
