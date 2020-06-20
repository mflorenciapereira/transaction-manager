package com.florencia.agileengine.service.impl;

import com.florencia.agileengine.dao.TransactionRepository;
import com.florencia.agileengine.domain.Transaction;
import com.florencia.agileengine.domain.TransactionType;
import com.florencia.agileengine.exception.InvalidTransactionException;
import com.florencia.agileengine.exception.TransactionNotFoundException;
import com.florencia.agileengine.service.TransactionService;
import com.florencia.agileengine.util.Constants;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    private TransactionRepository transactionRepository;


    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);


    @Override
    public List<Transaction> all() {
        return transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "effectiveDate"));
    }

    @Override
    public Transaction findById(String id) {
        validId(id);
        return transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Override
    public String save(String type, Double amount) {
        validInput(type, amount);
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setAccountId(Constants.ACCOUNT_ID);
        transaction.setEffectiveDate(new Date());
        return transactionRepository.save(transaction).getId();
    }

    @Override
    public Double getBalance() {
        return transactionRepository.getTotalCredit()-transactionRepository.getTotalDebit();
    }


    private void validId(String id) {
        if(Strings.isBlank(id)){
            throw new IllegalArgumentException("Id is required");
        }
    }

    private void validInput(String type, Double amount) {
        if(amount == null){
            throw new IllegalArgumentException("Amount is required");
        }

        if(Strings.isBlank(type)){
            throw new IllegalArgumentException("Type is required");
        }

        TransactionType transactionType = TransactionType.getType(type);

        if(transactionType == null){
            throw new IllegalArgumentException("Unknown transaction type");
        }

        Double total = transactionRepository.getTotalCredit() - transactionRepository.getTotalDebit();

        Double newTotal = total + transactionType.getFactor() * amount;

        if(newTotal < 0 ){
            throw new InvalidTransactionException("Transaction makes the amount negative");
        }

    }


}
