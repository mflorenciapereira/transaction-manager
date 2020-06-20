package com.florencia.agileengine.controller;

import com.florencia.agileengine.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/default")
public class DefaultController {

    @Autowired
    private TransactionService transactionService;

    private final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @GetMapping
    public ResponseEntity<Double> balance() {
        try {
            logger.info("Getting balance");
            Double balance = transactionService.getBalance();
            return ResponseEntity.ok(balance);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

}