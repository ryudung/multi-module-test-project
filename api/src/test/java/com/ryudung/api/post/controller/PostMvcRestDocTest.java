package com.ryudung.api.post.controller;

import com.ryudung.api.helper.ApiDocumentUtils;
import com.ryudung.api.helper.BaseRestDocTestHelper;
import com.ryudung.api.helper.annotation.WebMvcRestDocTest;
import com.ryudung.api.post.PostService;
import com.ryudung.domain.board.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostRestController.class)
@WebMvcRestDocTest
class PostMvcRestDocTest extends BaseRestDocTestHelper {
    @MockBean
    private PostService postService;

    @Test
    @DisplayName("post id로 가져오기 테스트")
    void getPost() throws Exception {

        //given
        given(postService.find(1L))
                .willReturn(Post.builder()
                        .id(1L)
                        .title("타이틀")
                        .content("내용")
                        .build());

        //when
        ResultActions result = this.mockMvc.perform(get("/post/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/detail",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("글 id")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("글 id"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("글 내용")
                        )
                ));
    }
}