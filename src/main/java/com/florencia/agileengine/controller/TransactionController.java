package com.florencia.agileengine.controller;

import com.florencia.agileengine.domain.Transaction;
import com.florencia.agileengine.dto.TransactionDTO;
import com.florencia.agileengine.dto.TransactionHistoryDTO;
import com.florencia.agileengine.exception.TransactionNotFoundException;
import com.florencia.agileengine.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/transactions")
@CrossOrigin(origins = {"http://localhost:4200"})
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping
    public ResponseEntity<List<TransactionHistoryDTO>> all() {
        try {
            logger.info("Getting all the transactions");
            List<TransactionHistoryDTO> transactionHistoryDTOS = transactionService.all().stream().map(t -> new TransactionHistoryDTO(t)).collect(Collectors.toList());
            return ResponseEntity.ok(transactionHistoryDTOS);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<TransactionHistoryDTO> findById(@PathVariable String id) {
        try {
            logger.info("Getting the transaction with id {}", id);
            Transaction transaction = transactionService.findById(id);
            return ResponseEntity.ok(new TransactionHistoryDTO(transaction));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in argument", e.getCause());
        } catch (TransactionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<String> commit(@RequestBody TransactionDTO transactionDTO) {
        try {
            logger.info("Commiting transaction " + transactionDTO);
            String id = transactionService.save(transactionDTO.getType(), transactionDTO.getAmount());
            return ResponseEntity.ok(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
