package com.ryudung.api.post.controller;

import com.ryudung.api.post.PostService;
import com.ryudung.domain.board.Post;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Post Rest 컨트롤러
 * */
@AllArgsConstructor
@RequestMapping("/")
@RestController
public class PostRestController {
    private final PostService postService;

    @GetMapping("/post/{id}")
    public Post getPost(@PathVariable(value = "id") Long id) {
        return postService.find(id);
    }
}
