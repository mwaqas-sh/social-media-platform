package com.waqas.social.media.platform.service;

import com.waqas.social.media.platform.domain.Post;
import com.waqas.social.media.platform.domain.User;
import com.waqas.social.media.platform.repository.PostRepository;
import com.waqas.social.media.platform.repository.UserRepository;
import com.waqas.social.media.platform.request.PostRequest;
import com.waqas.social.media.platform.utils.HttpRequestResult;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private HttpServletRequest httpServletRequest ;

    @BeforeEach
    void setUp() throws Exception {
        ReflectionTestUtils.setField(postService, "jwtSecret", "011cffcdb8e935d281df69a37d7b091a5096f2f6f4170baa54f849c569bcfe9a");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

    }

    @Test
    void testCreatePost_Success() throws Exception {
        when(httpServletRequest.getHeader(any())).thenReturn("Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjIsImlhdCI6MTczODkyNTQ3MywiZXhwIjoxNzM5NDI1NDczfQ.OrqBjJleQzhu-6p7rOeAqMlR59N99Tw4JFdSZ2p6qqg");
        Post post = mock(Post.class);
        User user = mock(User.class);
        PostRequest postRequest = mock(PostRequest.class);
        when(userRepository.findByUserId(anyLong())).thenReturn(user);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        ResponseEntity<HttpRequestResult> result = postService.createPost(postRequest);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testCreatePost_UserNotFound() throws Exception {
        when(httpServletRequest.getHeader(any())).thenReturn("Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjIsImlhdCI6MTczODkyNTQ3MywiZXhwIjoxNzM5NDI1NDczfQ.OrqBjJleQzhu-6p7rOeAqMlR59N99Tw4JFdSZ2p6qqg");
        Post post = mock(Post.class);
        PostRequest postRequest = mock(PostRequest.class);
        when(userRepository.findByUserId(anyLong())).thenReturn(null);
        ResponseEntity<HttpRequestResult> result = postService.createPost(postRequest);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getAllPosts() {
    }

    @Test
    void getPostById() {
    }

    @Test
    void updatePostById() {
    }

    @Test
    void deletePostById() {
    }

    @Test
    void commentPost() {
    }

    @Test
    void likePost() {
    }
}