package com.example.account.constatnt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Code {
    SUCESS("0000", "성공하였습니다."),
    UNREGISTER_SUCCESS("0000", "해지 성공하였습니다."),
    DEPOSIT_SUCCESS("0000", "입금 성공하였습니다."),
    WITHDRAW_SUCCESS("0000", "출금 성공하였습니다."),
    PAY_SUCCESS("0000", "결제 성공하였습니다."),
    CANCEL_SUCCESS("0000", "결제 취소에 성공하였습니다.");

    private String code;
    private String message;
}
