package com.florencia.agileengine.domain;

import java.util.Arrays;

public enum TransactionType {
    CREDIT("credit", 1),
    DEBIT("debit", -1);

    private String code;
    private Integer factor;

    TransactionType(String code, Integer factor) {
        this.code = code;
        this.factor = factor;
    }

    public String getCode() {
        return code;
    }

    public Integer getFactor() {
        return factor;
    }

    public static TransactionType getType(String code){
        return Arrays.stream(TransactionType.values()).filter(type -> type.getCode().equals(code)).findFirst().get();
    }
}
