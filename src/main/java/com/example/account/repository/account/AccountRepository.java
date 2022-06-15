package com.example.account.repository.account;

import com.example.account.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumberAndMemberId(String accountNumber, Long memberId);

    @Query("select max(a.id) from Account a")
    Optional<Integer> findMaxId();

}
