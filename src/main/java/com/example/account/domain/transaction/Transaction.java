package com.example.account.domain.transaction;

import com.example.account.domain.account.Account;
import com.example.account.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long balance;
    private String accountNumber;
    private Long accountId;
    private Long transactionResult;

    @Enumerated(EnumType.STRING)
    private TransactionStatus accountStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", updatable = false, insertable = false)
    private Account account;

    @Builder
    public Transaction(Long id, Long balance, TransactionStatus accountStatus,
                       String accountNumber, Long accountId, Long transactionResult) {
        this.id = id;
        this.balance = balance;
        this.accountStatus = accountStatus;
        this.accountNumber = accountNumber;
        this.accountId = accountId;
        this.transactionResult = transactionResult;
    }

    public void cancel(Long amount) {
        this.accountStatus = TransactionStatus.CANCEL;
        this.transactionResult += amount;
    }

}
