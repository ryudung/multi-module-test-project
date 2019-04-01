package com.ryudung.jpa.transactional;

import com.ryudung.jpa.helper.AbstractSpringBootTest;
import com.ryudung.jpa.transactional.pojo.TransactionInfo;
import com.ryudung.jpa.transactional.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.NestedTransactionNotSupportedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ============================================================================
 * propagation
 * ============================================================================
 * REQUIRED(디폴트 값)
 *      현재 트랜잭션을 지원하고 존재하지 않으면 새 트랜잭션을 생성하여 사용.
 * REQUIRES_NEW
 *      새 트랜잭션을 만들고 현재 트랜잭션을 사용하지 않는다.
 * MANDATORY
 *      현재 트랜잭션을 지원하고 존재하지 않으면 예외를 throw한다.
 * NESTED
 *      현재 트랜잭션이 존재하면 중첩 트랜잭션 내에서 실행하고,
 *      존재하지 않으면PROPAGATION_REQUIRED처럼 동작한다.
 * SUPPORTS
 *      현재 트랜잭션을 지원하고 트랜잭션이 존재하지 않으면 비트랜잭션으로 실행한다.
 * NOT_SUPPORTED
 *      비 트랜잭션 적으로 실행하고, 현재 트랜잭션으로 사용하지 않는다.
 * NEVER
 *      비 트랜잭션 적으로 실행하고, 트랜잭션이 존재하면 예외를 던진다.
 *
 *
 *
 * 트랜잭션 확인.
 * ref: doc:https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/transaction/support/TransactionSynchronizationManager.html
 *
 *
 */
@Slf4j
@Import(TestService.class)
public class PropagationTest extends AbstractSpringBootTest {

    @Autowired
    private TestService testService;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("transaction이 없을 경우 null")
    @Test
    void test1() {
        //given
        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        //then
        assertNull(currentTransactionName);
    }

    @DisplayName("REQUIRED: 부모 트랜잭션 존재할 경우, 부모 트랜잭션 사용")
    @Transactional
    @Test
    void test3() {

        //give
        TransactionInfo childTransaction = testService.REQUIRED();
        String parentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        //info
        log.info(parentTransactionName);
        log.info(childTransaction.toString());

        //when
        assertEquals(parentTransactionName, childTransaction.getName());
        assertTrue(childTransaction.isActualTransactionActive());
    }

    @DisplayName("REQUIRES_NEW: 부모 트랜잭션 존재할 경우, 자식 트랜잭션 사용.")
    @Transactional
    @Test
    void test4() {
        //give
        TransactionInfo childTransaction = testService.REQUIRES_NEW();
        String parentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        //info
        log.info(parentTransactionName);
        log.info(childTransaction.toString());

        //when
        assertTrue(childTransaction.isActualTransactionActive());
    }


    @DisplayName("MANDATORY: 부모 트랜잭션 존재할 경우, 부모 트랜잭션 사용.")
    @Test
    @Transactional
    public void test5(){
        //give
        TransactionInfo childTransaction = testService.MANDATORY();
        String parentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        //info
        log.info(parentTransactionName);
        log.info(childTransaction.toString());

        //when
        assertEquals(parentTransactionName, childTransaction.getName());
        assertTrue(childTransaction.isActualTransactionActive());
    }

    @DisplayName("MANDATORY: 부모 트랜잭션이 없을경우, 에러!")
    @Test
    public void test6(){
        //give
        String parentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        //info
        log.info(parentTransactionName);

        //when & then
        assertThrows(
                IllegalTransactionStateException.class,  // 예상 에러
                ()-> testService.MANDATORY(), // 에러 발생 메소드!
                "No existing transaction found for transaction marked with propagation 'mandatory'"// 예상 에러 메시지.
        );
    }

    @DisplayName("NESTED: JpaDialect가 제공하지 않음.")
    @Test
    @Transactional
    public void test7(){
        //when & then
        assertThrows(
                NestedTransactionNotSupportedException.class,
                ()-> testService.NESTED(),
                "JpaDialect does not support savepoints - check your JPA provider's capabilities"
        );
    }


    @DisplayName("SUPPORTS: 부모 트랜잭션이 존재할 경우, 부모 트랜잭션 사용.")
    @Test
    @Transactional
    public void test8(){
        //give
        TransactionInfo childTransaction = testService.SUPPORTS();
        String parentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        //info
        log.info(parentTransactionName);

        //then
        assertEquals(parentTransactionName, childTransaction.getName());
        assertTrue(childTransaction.isActualTransactionActive());
    }

    /**
     * 비 트랜잭션은 트랜잭션 매니저에 의해서 관리되고, 실제 트랜젝션 기능은 지원하지 않는다.
     * */
    @DisplayName("SUPPORTS: 부모 트랜잭션이 존재 하지 않을 경우, 비 트랜잭션 사용.")
    @Test
    public void test9(){
        //give
        TransactionInfo childTransaction = testService.SUPPORTS();
        String parentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        //info
        log.info(parentTransactionName);

        //then
        assertFalse(childTransaction.isActualTransactionActive());
    }

    @DisplayName("NOT_SUPPORTED: 부모 트랜잭션이 존재하지 않을 경우, 비 트랜잭션을 사용한다.")
    @Test
    public void test10(){
        //give
        TransactionInfo childTransaction = testService.NOT_SUPPORTED();
        String parentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        //info
        log.info(parentTransactionName);
        log.info(childTransaction.toString());

        //then
        assertFalse(childTransaction.isActualTransactionActive());
    }

    @DisplayName("NOT_SUPPORTED: 부모 트랜잭션이 존재할 경우, 비 트랜잭션을 사용한다.")
    @Test
    @Transactional
    public void test11(){

        //give
        TransactionInfo childTransaction = testService.NOT_SUPPORTED();
        String parentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        //info
        log.info(parentTransactionName);
        log.info(childTransaction.toString());

        //then
        assertFalse(childTransaction.isActualTransactionActive());
    }

    @DisplayName("NEVER: 부모 트랜잭션이 없을 경우, 비 트랜잭션을 사용한다.")
    @Test
    public void test12(){
        //give
        TransactionInfo childTransaction = testService.NEVER();
        String parentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        //info
        log.info(parentTransactionName);
        log.info(childTransaction.toString());

        //then
        assertFalse(childTransaction.isActualTransactionActive());
    }

    @DisplayName("NEVER: 부모 트랜잭션이 존재할 경우, 에러")
    @Test
    @Transactional
    public void test13(){
        //when & then
        assertThrows(
                IllegalTransactionStateException.class,
                ()-> testService.NEVER(),
                "Existing transaction found for transaction marked with propagation 'never'"
        );
    }

    //###########################################################################
    /**
     * 읽기만 하는 경우에는 @transactional(readOnly=true)를 사용하여, dirty checking을 하지않고 성능에 도움을 줄 수있다.
     * 추가하여,
     * readOnly = true시에 REQUIRED 보다는 NOT_SUPPORT를 사용하여 비 트랜잭션을 통해 플러시를 사용하지 않는다.
     * */
    @DisplayName("NOT_SUPPORTS_readOnly_true 일 경우")
    @Test
    public void readOnlyTrue(){
        //given
        TransactionInfo transactionInfo = testService.NOT_SUPPORTS_readOnly_true();

        //then
        assertTrue(transactionInfo.isCurrentTransactionReadOnly());
    }

}
