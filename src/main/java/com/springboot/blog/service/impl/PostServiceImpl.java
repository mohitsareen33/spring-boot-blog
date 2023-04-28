package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
//    constructor based dependency injection
//    public PostServiceImpl(PostRepository postRepository) {
//    this.postRepository = postRepository;
//}

    private ModelMapper mapper;
//    since we are using constructor based dependency injection we need to configure it in constructor
    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {

        this.postRepository = postRepository;
        this.mapper = mapper;
    }



    @Override
    public PostDto createPost(PostDto postDto) {

//        convert dto to entity
        Post post = mapToEntity(postDto);

        Post newPost = postRepository.save(post);

//        convert entity to dto
        PostDto postResponse = mapToDto(newPost);

        return postResponse;
    }

//    get all posts
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir){

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

//        create pageable instance
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//      PageRequest.of provides pagination support
//      If we use PageRequest.of with 3 arguments, it will provide pagination and sorting support

        Page<Post> posts = postRepository.findAll(pageable);
//        Page class has getContent method to get content for page object

//        get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content = listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;


//        normal getAllPosts method(without pagination)
//        List<Post> posts = postRepository.findAll();
//        return posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
//        post = mapToEntity(postDto);
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);

    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

//    convert entity to dto using model mapper
    private PostDto mapToDto(Post post){
        PostDto postDto = mapper.map(post, PostDto.class);
        return postDto;
    }

//    convert dto to entity using model mapper
    private Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);
        return post;
    }


    //    convert entity to dto
//    private PostDto mapToDto(Post post){
//        PostDto postDto = new PostDto();
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
//
//        return postDto;
//
//    }

//    convert dto to entity
//    private Post mapToEntity(PostDto postDto){
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
//        return post;
//    }

}

