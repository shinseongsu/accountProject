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

    private String orderName;
    private Long balance;

    @Enumerated(EnumType.STRING)
    private TransactionStatus accountStatus;

    private String accountNumber;

    private Long accountId;

    @ManyToOne
    @JoinColumn(name = "accountId", updatable = false, insertable = false)
    private Account account;

    @Builder
    public Transaction(Long id, String orderName, Long balance, TransactionStatus accountStatus,
                       String accountNumber, Long accountId) {
        this.id = id;
        this.orderName = orderName;
        this.balance = balance;
        this.accountStatus = accountStatus;
        this.accountNumber = accountNumber;
        this.accountId = accountId;
    }

    public void cancel() {
        this.accountStatus = TransactionStatus.CANCEL;
    }

}
