package com.example.account.service.transaction;

import com.example.account.constatnt.Code;
import com.example.account.domain.account.Account;
import com.example.account.domain.transaction.Transaction;
import com.example.account.domain.transaction.TransactionStatus;
import com.example.account.dto.transcation.*;
import com.example.account.exception.AccountException;
import com.example.account.repository.account.AccountRepository;
import com.example.account.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DepositDto.Response deposit(DepositDto depositDto) {
        Account account = accountRepository.findByAccountNumber(depositDto.getAccountNumber())
                .orElseThrow(() -> new AccountException("계좌가 존재하지 않습니다."));
        if(depositDto.getAmount() <= 0) throw new AccountException("거래 금액 값이 너무 작습니다.");
        if(!depositDto.getMemberId().equals(account.getMemberId())) throw new AccountException("소유자가 다릅니다.");
        account.deposit(depositDto.getAmount());

        Transaction transaction = Transaction.builder()
                .balance(depositDto.getAmount())
                .accountStatus(TransactionStatus.DEPOSIT)
                .accountNumber(depositDto.getAccountNumber())
                .accountId(account.getId())
                .transactionResult(account.getBalance())
                .build();
        transactionRepository.save(transaction);

        return DepositDto.Response.builder()
                .code(Code.DEPOSIT_SUCCESS.getCode())
                .message(Code.DEPOSIT_SUCCESS.getMessage())
                .accountNumber(depositDto.getAccountNumber())
                .transactionResult(transaction.getTransactionResult())
                .transactionId(transaction.getId())
                .amount(depositDto.getAmount())
                .transactionDate(transaction.getCreateDate())
                .build();
    }

    public WithdrawDto.Response withdraw(WithdrawDto withdrawDto) {
        Account account = accountRepository.findByAccountNumber(withdrawDto.getAccountNumber())
                .orElseThrow(() -> new AccountException("계좌가 존재하지 않습니다."));
        if(withdrawDto.getAmount() <= 0) throw new AccountException("거래 금액 값이 너무 작습니다.");
        if(!withdrawDto.getMemberId().equals(account.getMemberId())) throw new AccountException("소유자가 다릅니다.");
        account.withdraw(Long.valueOf(withdrawDto.getAmount()));

        Transaction transaction = Transaction.builder()
                .balance(Long.valueOf(withdrawDto.getAmount()))
                .accountStatus(TransactionStatus.WITHDRAW)
                .accountNumber(withdrawDto.getAccountNumber())
                .accountId(account.getId())
                .transactionResult(account.getBalance())
                .build();
        transactionRepository.save(transaction);

        return WithdrawDto.Response.builder()
                .code(Code.DEPOSIT_SUCCESS.getCode())
                .message(Code.DEPOSIT_SUCCESS.getMessage())
                .accountNumber(withdrawDto.getAccountNumber())
                .transactionResult(transaction.getTransactionResult())
                .transactionId(transaction.getId())
                .amount(withdrawDto.getAmount())
                .transactionDate(transaction.getCreateDate())
                .build();
    }

    public PayDto.Response pay(PayDto payDto) {
        Account account = accountRepository.findByAccountNumber(payDto.getAccountNumber())
                .orElseThrow(() -> new AccountException("계좌가 존재하지 않습니다."));
        if(payDto.getAmount() <= 0) throw new AccountException("거래 금액 값이 너무 작습니다.");
        if(!payDto.getMemberId().equals(account.getMemberId())) throw new AccountException("소유자가 다릅니다.");
        account.withdraw(Long.valueOf(payDto.getAmount()));

        Transaction transaction = Transaction.builder()
                .balance(Long.valueOf(payDto.getAmount()))
                .accountStatus(TransactionStatus.PAY)
                .accountNumber(payDto.getAccountNumber())
                .accountId(account.getId())
                .transactionResult(account.getBalance())
                .build();
        transactionRepository.save(transaction);

        return PayDto.Response.builder()
                .code(Code.PAY_SUCCESS.getCode())
                .message(Code.PAY_SUCCESS.getMessage())
                .accountNumber(payDto.getAccountNumber())
                .transactionResult(transaction.getTransactionResult())
                .transactionId(transaction.getId())
                .amount(payDto.getAmount())
                .transactionDate(transaction.getCreateDate())
                .build();
    }

    public CancelDto.Response cancel(CancelDto cancelDto) {
        Account account = accountRepository.findByAccountNumber(cancelDto.getAccountNumber())
                .orElseThrow(() -> new AccountException("계좌가 존재하지 않습니다."));
        Transaction transaction = transactionRepository.findById(Long.valueOf(cancelDto.getTransactionId()))
                .orElseThrow(() -> new AccountException("거래 정보가 존재하지 않습니다."));
        if(!transaction.getBalance().equals(cancelDto.getAmount())) throw new AccountException("결제 금액이 달라 취소할 수 없습니다.");
        if(transaction.getAccountStatus().equals(TransactionStatus.CANCEL)) throw new AccountException("이미 취소 상태입니다.");
        if(!transaction.getAccountStatus().equals(TransactionStatus.PAY)) throw new AccountException("결제만 취소 가능합니다.");

        transaction.cancel(cancelDto.getAmount());
        account.deposit(cancelDto.getAmount());

        return CancelDto.Response.builder()
                .code(Code.CANCEL_SUCCESS.getCode())
                .message(Code.CANCEL_SUCCESS.getMessage())
                .accountNumber(cancelDto.getAccountNumber())
                .transactionResult(transaction.getTransactionResult())
                .transactionId(cancelDto.getTransactionId())
                .amount(cancelDto.getAmount())
                .transactionDate(transaction.getModifiedDate())
                .build();
    }

    public TransactionDto transaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AccountException("거래 정보가 존재하지 않습니다."));

        return TransactionDto.builder()
                .code(Code.SUCESS.getCode())
                .message(Code.SUCESS.getMessage())
                .accountNumber(transaction.getAccountNumber())
                .transactionStatus(transaction.getAccountStatus())
                .transactionResult(transaction.getTransactionResult())
                .transactionId(transaction.getId())
                .amount(transaction.getBalance())
                .transactionDate(transaction.getModifiedDate())
                .build();
    }



}
