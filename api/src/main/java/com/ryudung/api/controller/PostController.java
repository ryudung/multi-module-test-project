package com.ryudung.api.controller;

import com.ryudung.domain.board.Post;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class PostController {

    @GetMapping("/post/{id}")
    public Post getPost(@PathVariable(value = "id") Long id){
        return Post.builder()
                .id(id)
                .title("title")
                .content("content")
                .build();
    }
}
