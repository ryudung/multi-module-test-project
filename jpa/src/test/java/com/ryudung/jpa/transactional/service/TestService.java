package com.ryudung.jpa.transactional.service;

import com.ryudung.jpa.transactional.pojo.TransactionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.function.Supplier;


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
 */
@Slf4j
@Service
@TestConfiguration
public class TestService {

    @Transactional(propagation = Propagation.REQUIRED)
    public TransactionInfo REQUIRED(){
        return getAndLoggingSupplier.get();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TransactionInfo REQUIRES_NEW(){
        return getAndLoggingSupplier.get();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public TransactionInfo MANDATORY(){
        return getAndLoggingSupplier.get();
    }

    @Transactional(propagation = Propagation.NESTED)
    public TransactionInfo NESTED(){
        return getAndLoggingSupplier.get();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public TransactionInfo SUPPORTS(){
        return getAndLoggingSupplier.get();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public TransactionInfo NOT_SUPPORTS_readOnly_true(){
        return getAndLoggingSupplier.get();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public TransactionInfo NOT_SUPPORTED(){
        return getAndLoggingSupplier.get();
    }

    @Transactional(propagation = Propagation.NEVER)
    public TransactionInfo NEVER(){
        return getAndLoggingSupplier.get();
    }

    private Supplier<TransactionInfo> getAndLoggingSupplier = () -> TransactionInfo.builder()
            .name(TransactionSynchronizationManager.getCurrentTransactionName())
            .isActualTransactionActive(TransactionSynchronizationManager.isActualTransactionActive())
            .isCurrentTransactionReadOnly(TransactionSynchronizationManager.isCurrentTransactionReadOnly())
            .build();
}
