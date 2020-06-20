package com.florencia.agileengine.exception;

public class TransactionNotFoundException extends RuntimeException{

    public TransactionNotFoundException(String id) {
        super("Transaction with "+id +" not found");
    }

}
