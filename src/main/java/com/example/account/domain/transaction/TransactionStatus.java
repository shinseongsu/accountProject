package com.example.account.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionStatus {

    DEPOSIT("입금"),
    WITHDRAW("출금"),
    PAY("결제"),
    CANCEL("취소");

    private String value;



}
