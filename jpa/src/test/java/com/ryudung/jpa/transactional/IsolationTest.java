package com.ryudung.jpa.transactional;

import com.ryudung.jpa.helper.AbstractSpringBootTest;
import com.ryudung.jpa.transactional.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 *
 * ============================================================================
 * Isolation
 * ============================================================================
 * @see java.sql.Connection
 *
 *     int ISOLATION_DEFAULT = -1;
 *     int ISOLATION_READ_UNCOMMITTED = 1;
 *     int ISOLATION_READ_COMMITTED = 2;
 *     int ISOLATION_REPEATABLE_READ = 4;
 *     int ISOLATION_SERIALIZABLE = 8;
 *
 * 트랜잭션 확인.
 * ref: doc:https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/transaction/support/TransactionSynchronizationManager.html
 *
 */


@Slf4j
@Import(TestService.class)
public class IsolationTest extends AbstractSpringBootTest {

    @Autowired
    private TestService testService;

    @Test
    @DisplayName("")
    void name() {
        //given
        //when
        //then
    }
}

