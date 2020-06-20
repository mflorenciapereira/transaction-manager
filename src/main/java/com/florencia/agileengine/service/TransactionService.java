package com.florencia.agileengine.service;

import com.florencia.agileengine.domain.Transaction;

import java.util.List;

public interface TransactionService {

   List<Transaction> all();

   Transaction findById(String id);

   String save(String type, Double amount);

   Double getBalance();
}
