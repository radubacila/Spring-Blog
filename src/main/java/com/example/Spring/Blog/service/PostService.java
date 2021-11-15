package com.example.Spring.Blog.service;

import com.example.Spring.Blog.dto.PostDto;
import com.example.Spring.Blog.exception.PostNotFoundException;
import com.example.Spring.Blog.model.Post;
import com.example.Spring.Blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private AuthService authService;
    @Autowired
    private PostRepository postRepository;

    public List<PostDto> showAllPosts()
    {
        List<Post> posts=postRepository.findAll();
        return posts.stream().map(this::mapFromPostToDto).collect(Collectors.toList());
    }

    public void createPost(PostDto postDto)
    {
        Post post=mapFromDtoToPost(postDto);
        postRepository.save(post);
    }

    public PostDto readSinglePost(Long id) {
        Post post=postRepository.findById(id).orElseThrow(()->new PostNotFoundException("For id "+id));
        return mapFromPostToDto(post);
    }

    private Post mapFromDtoToPost(PostDto postDto) {
        Post post=new Post();
        post.setContent(postDto.getContent());
        post.setTitle(postDto.getTitle());
        User loggedInUser=authService.getCurrentUser().orElseThrow(()->new IllegalArgumentException("User not found"));
        post.setUsermame(loggedInUser.getUsername());
        post.setCreatedOn(Instant.now().toString());
        post.setUpdatedOn(Instant.now().toString());
        return post;
    }

    private PostDto mapFromPostToDto(Post post)
    {
        PostDto postDto=new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setUsername(post.getUsername());
        return postDto;
    }

}
