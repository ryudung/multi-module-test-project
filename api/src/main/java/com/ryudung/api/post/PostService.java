package com.ryudung.api.post;

import com.ryudung.domain.board.Post;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    /**
     * post 정보를 찾는 메서드.
     *
     * @param id 찾을 Post id
     * @return 찾은 Post 객체
     */
    public Post find(Long id) {
        return Post.builder()
                .id(id)
                .title("타이틀")
                .content("내용")
                .build();
    }
}
