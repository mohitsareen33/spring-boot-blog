package com.springboot.blog.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    private long id;

//    Title should not be null or empty and should have atleast two characters
    @NotEmpty
    @Size(min = 2, message = "Post title should have atleast 2 characters")
    private String title;

//    Description should not be null or empty and should have atleast 10 characters
    @NotEmpty
    @Size(min = 10, message = "Post description should have atleast 10 characters")
    private String description;

//    content should not be empty
    @NotEmpty
    private String content;

//    to get comments with posts
    private Set<CommentDto> comments;
}
