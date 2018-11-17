package com.ryudung.jpa.crud;

import com.ryudung.domain.board.Post;
import com.ryudung.domain.board.PostRepository;
import com.ryudung.jpa.helper.AbstractDataJpaTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.support.AbstractEntityInformation;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
public class CrudTest extends AbstractDataJpaTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager entityManager;


    @DisplayName("presist() 트랜잭션이 존재하지 않을 경우")
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void test() {

        //given
        Post post = Post.builder()
                .title("타이틀")
                .content("컨텐츠")
                .build();

        //then
        assertThrows(TransactionRequiredException.class,
                () -> entityManager.persist(post),
                "No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call");
    }

    @Rollback(value = false)
    @DisplayName("persist() 변경감지 확인")
    @Test
    public void test1() {

        //given
        Post post = Post.builder()
                .title("타이틀")
                .content("컨텐츠")
                .build();

        //when
        entityManager.persist(post); //return void

        /**
         *  persist()를 통해 post 객체가 영속성 관리 대상이 된다.
         * */

        post.setContent("컨텐츠 변경3");


        //then
        Post findPost = entityManager.find(Post.class, 1L);
        assertEquals(post.getContent(), findPost.getContent());
    }

    @Rollback(value = false)
    @DisplayName("merge() 변경감지 확인")
    @Test
    public void test2() {

        //given
        Post post = Post.builder()
                .id(2L)
                .title("타이틀")
                .content("컨텐츠")
                .build();

        //when
        Post mergedPost = entityManager.merge(post);//return post

        /**
         *  merge() 통해 복사된 mergedPost가 영속성 관리대상이 되고,
         *  post는 관리되지 않는다.
         * */

        mergedPost.setContent("컨텐츠 변경1");
        post.setContent("컨텐츠 변경2");


        //then
        Post findPost = entityManager.find(Post.class, 2L);
        assertAll(
                () -> assertEquals(mergedPost.getContent(), findPost.getContent()),
                () -> assertNotEquals(post.getContent(), findPost.getContent())
        );
    }

    @Rollback(value = false)
    @DisplayName("merge()에서 select()가 발생하는 경우")
    @Test
    public void test3() {
        //given
        Post post = Post.builder()
                .title("타이틀")
                .content("컨텐츠")
                .build();

        entityManager.persist(post);

        entityManager.detach(post);

        /**
         * detach 상태(또는 id값이 존재하는 객체)의 객체를  merge()할 경우,
         * 아무것도 안할지/ update 할지/ insert를 할지를 판단하기위해서 select()한다.
         * */

        Post mergedPost = entityManager.merge(post);
        //Post mergedPost = entityManager.merge(Post.builder().id(1L).build());
    }


    /**
     * @see AbstractEntityInformation
     */
    @Test
    @DisplayName("save() id가 존재할 경우, merge()")
    @Rollback(value = false)
    void test4() {
        //given
        Post post = Post.builder()
                .id(1L)
                .content("컨텐츠")
                .title("타이틀")
                .build();

        //then
        postRepository.save(post);
    }

    /**
     * @see AbstractEntityInformation
     */
    @Test
    @DisplayName("save() id가 존재하지 않을 경우, persist()")
    @Rollback(value = false)
    void test5() {
        //given
        Post post = Post.builder()
                .content("컨텐츠")
                .title("타이틀")
                .build();

        //then
        postRepository.save(post);
    }
}
