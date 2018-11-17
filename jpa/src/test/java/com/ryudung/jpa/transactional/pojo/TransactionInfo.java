package com.ryudung.jpa.transactional.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class TransactionInfo {
    String name;
    boolean isActualTransactionActive;
    boolean isCurrentTransactionReadOnly;
}
