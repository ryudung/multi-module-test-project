package com.ryudung.jpa;

import com.ryudung.domain.board.Post;
import com.ryudung.domain.board.PostRepository;
import com.ryudung.jpa.helper.AbstractDataJpaTest;
import com.ryudung.jpa.helper.AbstractSpringBootTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@Slf4j
@Rollback(value = true)
class Test01ApplicationTest extends AbstractDataJpaTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("merge() 테스트")
    @Test
//    @Transactional
    public void test(){

        //given
        Post post = Post.builder()
                .title("타이틀")
                .content("컨텐츠")
                .build();

        //when
        Post persistedPost = entityManager.persist(post);
        persistedPost.setContent("변경된 컨텐츠");

        //then


    }
}