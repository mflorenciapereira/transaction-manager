package com.florencia.agileengine.dao;

import com.florencia.agileengine.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="5000")})
    Transaction save(Transaction entity);

    @Query(value = "select coalesce(sum(amount),0) from transaction where type = 'credit'", nativeQuery = true)
    Double getTotalCredit();

    @Query(value = "select coalesce(sum(amount),0) from transaction where type = 'debit'", nativeQuery = true)
    Double getTotalDebit();

}