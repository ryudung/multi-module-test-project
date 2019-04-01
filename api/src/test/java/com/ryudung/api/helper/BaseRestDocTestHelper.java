package com.ryudung.api.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
/**
 * 기본 테스트 클래스
 * */
public class BaseRestDocTestHelper {

    @Autowired
    protected MockMvc mockMvc;

    //post 요청에 사용.
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        objectMapper = new ObjectMapper();
    }
}
