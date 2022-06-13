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

    public Account deposit(DepositDto.Request request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new AccountException("계좌가 존재하지 않습니다."));
        account.deposit(  Long.valueOf(request.getAmount()));

        Transaction transaction = Transaction.builder()
                .orderName(TransactionStatus.DEPOSIT.getValue())
                .balance( Long.valueOf(request.getAmount()))
                .accountStatus(TransactionStatus.DEPOSIT)
                .accountNumber(request.getAccountNumber())
                .accountId(account.getId())
                .build();
        transactionRepository.save(transaction);
        return account;
    }

    public Account withdraw(WithdrawDto.Request request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new AccountException("계좌가 존재하지 않습니다."));
        if(!account.getPassword().equals(Base64.encodeBase64String(request.getPassword().getBytes()))) {
            throw new AccountException("패스워드가 맞지 않습니다.");
        }
        account.withdraw(Long.valueOf(request.getAmount()));

        Transaction transaction = Transaction.builder()
                .orderName(TransactionStatus.WITHDRAW.getValue())
                .balance(Long.valueOf(request.getAmount()))
                .accountStatus(TransactionStatus.WITHDRAW)
                .accountNumber(request.getAccountNumber())
                .accountId(account.getId())
                .build();
        transactionRepository.save(transaction);
        return account;
    }

    public PayDto.Response pay(PayDto.Request request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new AccountException("계좌가 존재하지 않습니다."));
        if(!account.getPassword().equals(Base64.encodeBase64String(request.getPassword().getBytes()))) {
            throw new AccountException("패스워드가 맞지 않습니다.");
        }
        account.withdraw(Long.valueOf(request.getAmount()));

        Transaction transaction = Transaction.builder()
                .orderName(request.getOrderName())
                .balance(Long.valueOf(request.getAmount()))
                .accountStatus(TransactionStatus.PAY)
                .accountNumber(request.getAccountNumber())
                .accountId(account.getId())
                .build();
        transactionRepository.save(transaction);

        return PayDto.Response.builder()
                .code(Code.PAY_SUCCESS.getCode())
                .message(Code.PAY_SUCCESS.getMessage())
                .orderId(transaction.getId())
                .orderName(request.getOrderName())
                .balance(Long.valueOf(request.getAmount()))
                .accountBalance(account.getBalance())
                .build();
    }

    public CancelDto.Response cancel(CancelDto.Request request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new AccountException("계좌가 존재하지 않습니다."));
        Transaction transaction = transactionRepository.findById(Long.valueOf(request.getOrderId()))
                .orElseThrow(() -> new AccountException("거래 정보가 존재하지 않습니다."));
        if(transaction.getAccountStatus().equals(TransactionStatus.CANCEL)) {
            throw new AccountException("이미 취소 상태입니다.");
        }
        if(!account.getPassword().equals(Base64.encodeBase64String(request.getPassword().getBytes()))) {
            throw new AccountException("패스워드가 맞지 않습니다.");
        }
        transaction.cancel();

        return CancelDto.Response.builder()
                .code(Code.CANCEL_SUCCESS.getCode())
                .message(Code.CANCEL_SUCCESS.getMessage())
                .orderId(transaction.getId())
                .orderName(transaction.getOrderName())
                .balance(transaction.getBalance())
                .accountBalance(account.getBalance())
                .build();
    }

    public TransactionDto transaction(String accountNumber, Pageable pageable) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException("계좌가 존재하지 않습니다."));

        Page<TransactionList> transactionLists = transactionRepository.findByAccountNumber(accountNumber, pageable);

        return TransactionDto.builder()
                .code(Code.SUCESS.getCode())
                .message(Code.SUCESS.getMessage())
                .accountNumber(accountNumber)
                .totalPage(transactionLists.getTotalPages())
                .transactionList(transactionLists.toList())
                .build();
    }



}
