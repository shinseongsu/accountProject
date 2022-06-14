package com.example.account.repository.transaction;

import com.example.account.domain.transaction.Transaction;
import com.example.account.dto.transcation.TransactionList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "select new com.example.account.dto.transcation.TransactionList(t.id, t.balance, t.accountStatus, t.modifiedDate) " +
            "from Transaction t " +
            "where t.accountNumber = :accountNumber"
            , countName = "select count(t) Transaction t where  t.accountNumber = :accountNumber")
    Page<TransactionList> findByAccountNumber(@Param("accountNumber") String accountNumber, Pageable pageable);

}
