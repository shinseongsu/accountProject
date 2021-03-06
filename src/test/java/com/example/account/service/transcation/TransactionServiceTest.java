package com.example.account.service.transcation;

import com.example.account.domain.account.Account;
import com.example.account.domain.account.AccountStatus;
import com.example.account.domain.transaction.Transaction;
import com.example.account.domain.transaction.TransactionStatus;
import com.example.account.dto.transcation.*;
import com.example.account.repository.account.AccountRepository;
import com.example.account.repository.member.MemberRepository;
import com.example.account.repository.transaction.TransactionRepository;
import com.example.account.service.account.AccountService;
import com.example.account.service.member.MemberService;
import com.example.account.service.transaction.TransactionService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TransactionServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Test
    @DisplayName("?????? ??????")
    void ??????_??????_TEST() throws Exception {
        String accountNumber = "1234567890";
        Long memberId = 1L;
        Long amount = 1000L;

        DepositDto request = DepositDto.builder()
                .accountNumber(accountNumber)
                .memberId(memberId)
                .amount(amount)
                .build();

        Account account = Account.builder()
                .id(1L)
                .memberId(memberId)
                .accountNumber(accountNumber)
                .balance(amount)
                .accountStatus(AccountStatus.IN_USE)
                .build();

        given(accountRepository.findByAccountNumber(request.getAccountNumber()))
                .willReturn(Optional.ofNullable(account));

        DepositDto.Response response = transactionService.deposit(request);

        assertAll(
                () -> assertEquals(response.getAccountNumber(), account.getAccountNumber()),
                () -> assertEquals(response.getTransactionResult(), account.getBalance()),
                () -> assertEquals(response.getAmount(), amount)
        );
    }

    @Test
    @DisplayName("?????? ?????? - ????????? ???????????? ?????? ???,")
    void ??????_??????_?????????_????????????_?????????_TEST() throws Exception {
        String accountNumber = "1234567890";
        Long memberId = 1L;
        Long amount = 1000L;

        DepositDto request = DepositDto.builder()
                .accountNumber(accountNumber)
                .memberId(memberId)
                .amount(amount)
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.deposit(request));

        assertEquals("????????? ???????????? ????????????.", exception.getMessage());
    }


    @Test
    @DisplayName("?????? ??????")
    void ??????_??????_TEST() throws Exception {
        String accountNumber = "1234567890";
        Long memberId = 1L;
        Long amount = 1000L;

        WithdrawDto request = WithdrawDto.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .memberId(memberId)
                .build();

        Account account = Account.builder()
                .id(1L)
                .memberId(memberId)
                .accountNumber(accountNumber)
                .balance(amount)
                .accountStatus(AccountStatus.IN_USE)
                .build();

        given(accountRepository.findByAccountNumber(accountNumber))
                .willReturn(Optional.ofNullable(account));

        WithdrawDto.Response response = transactionService.withdraw(request);

        assertAll(
                () -> assertEquals(response.getAccountNumber(), accountNumber),
                () -> assertEquals(response.getTransactionResult(), account.getBalance()),
                () -> assertEquals(response.getAmount(), amount)
        );
    }

    @Test
    @DisplayName("?????? ?????????, ????????? ???????????? ?????? ???")
    void ??????_?????????_?????????_????????????_?????????() throws Exception {
        String accountNumber = "1234567890";
        Long memberId = 1L;
        Long amount = 1000L;

        WithdrawDto request = WithdrawDto.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .memberId(memberId)
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.withdraw(request));
        assertEquals("????????? ???????????? ????????????.", exception.getMessage());
    }

    @Test
    @DisplayName("?????? ?????????")
    void ??????_?????????() throws Exception {
        String accountNumber = "1234567890";
        Long memberId = 1L;
        Long amount = 1000L;

        PayDto request = PayDto.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .memberId(memberId)
                .build();

        Account account = Account.builder()
                .id(1L)
                .memberId(memberId)
                .accountNumber(accountNumber)
                .balance(amount)
                .accountStatus(AccountStatus.IN_USE)
                .build();

        given(accountRepository.findByAccountNumber(accountNumber))
                .willReturn(Optional.ofNullable(account));

        PayDto.Response response = transactionService.pay(request);

        assertAll(
                () -> assertEquals(response.getAccountNumber(), accountNumber),
                () -> assertEquals(response.getTransactionResult(), account.getBalance()),
                () -> assertEquals(response.getAmount(), amount)
        );
    }

    @Test
    @DisplayName("[??????] ????????? ???????????? ?????????,")
    void ??????_?????????_????????????_?????????() throws Exception {
        String accountNumber = "1234567890";
        Long memberId = 1L;
        Long amount = 1000L;

        PayDto request = PayDto.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .memberId(memberId)
                .build();

        given(accountRepository.findByAccountNumber(accountNumber))
                .willReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.pay(request));
        assertEquals("????????? ???????????? ????????????.", exception.getMessage());
    }


    @Test
    @DisplayName("?????? ??????")
    void ??????_??????_TEST() throws Exception {
        String accountNumber = "1234567890";
        Long transactionId = 1L;
        Long amount = 1000L;
        Long memberId = 1L;

        CancelDto request = CancelDto.builder()
                .transactionId(transactionId)
                .accountNumber(accountNumber)
                .amount(amount)
                .build();

        Account account = Account.builder()
                .id(1L)
                .memberId(memberId)
                .accountNumber(accountNumber)
                .balance(amount)
                .accountStatus(AccountStatus.IN_USE)
                .build();

        Transaction transaction = Transaction.builder()
                .balance(amount)
                .accountNumber(accountNumber)
                .transactionResult(amount)
                .accountStatus(TransactionStatus.PAY)
                .build();

        given(accountRepository.findByAccountNumber(any()))
                .willReturn(Optional.ofNullable(account));

        given(transactionRepository.findById( anyLong() ))
                .willReturn(Optional.ofNullable(transaction));

        CancelDto.Response response = transactionService.cancel(request);

        assertAll(
                () -> assertEquals(response.getAccountNumber(), accountNumber),
                () -> assertEquals(response.getAmount(), amount),
                () -> assertEquals(response.getTransactionResult(), transaction.getTransactionResult())
        );
    }

    @Test
    @DisplayName("[?????? ??????] ????????? ????????? ?????? ??????")
    void ??????_??????_?????????_?????????_????????????() throws Exception {
        String accountNumber = "1234567890";
        Long transactionId = 1L;
        Long amount = 1000L;
        Long memberId = 1L;

        CancelDto request = CancelDto.builder()
                .transactionId(transactionId)
                .accountNumber(accountNumber)
                .amount(amount)
                .build();

        Account account = Account.builder()
                .id(1L)
                .memberId(memberId)
                .accountNumber(accountNumber)
                .balance(amount)
                .accountStatus(AccountStatus.IN_USE)
                .build();

        Transaction transaction = Transaction.builder()
                .balance(amount)
                .accountNumber(accountNumber)
                .transactionResult(amount)
                .accountStatus(TransactionStatus.DEPOSIT)
                .build();

        given(accountRepository.findByAccountNumber(request.getAccountNumber()))
                .willReturn(Optional.ofNullable(account));
        given(transactionRepository.findById( anyLong()))
                .willReturn(Optional.ofNullable(transaction));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.cancel(request));
        assertEquals("????????? ?????? ???????????????.", exception.getMessage());
    }


    @Test
    @DisplayName("[?????? ??????] ????????? ???????????? ?????? ???,")
    void ??????_??????_?????????_????????????_?????????() throws Exception {
        String accountNumber = "1234567890";
        Long transactionId = 1L;
        Long amount = 1000L;
        Long memberId = 1L;

        CancelDto request = CancelDto.builder()
                .transactionId(transactionId)
                .accountNumber(accountNumber)
                .amount(amount)
                .build();

        given(accountRepository.findByAccountNumber(request.getAccountNumber()))
                .willReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.cancel(request));
        assertEquals("????????? ???????????? ????????????.", exception.getMessage());
    }

    @Test
    @DisplayName("[?????? ??????] ?????? ????????? ???????????? ?????????")
    void ??????_??????_??????_?????????_????????????_?????????() throws Exception {
        String accountNumber = "1234567890";
        Long transactionId = 1L;
        Long amount = 1000L;
        Long memberId = 1L;

        CancelDto request = CancelDto.builder()
                .transactionId(transactionId)
                .accountNumber(accountNumber)
                .amount(amount)
                .build();

        Account account = Account.builder()
                .id(1L)
                .memberId(memberId)
                .accountNumber(accountNumber)
                .balance(amount)
                .accountStatus(AccountStatus.IN_USE)
                .build();

        given(accountRepository.findByAccountNumber(request.getAccountNumber()))
                .willReturn(Optional.ofNullable(account));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.cancel(request));
        assertEquals("?????? ????????? ???????????? ????????????.", exception.getMessage());
    }

    @Test
    @DisplayName("[?????? ??????] ?????? ?????? ????????????,")
    void ??????_??????_??????_??????_????????????() throws Exception {
        String accountNumber = "1234567890";
        Long transactionId = 1L;
        Long amount = 1000L;
        Long memberId = 1L;

        CancelDto request = CancelDto.builder()
                .transactionId(transactionId)
                .accountNumber(accountNumber)
                .amount(amount)
                .build();

        Account account = Account.builder()
                .id(1L)
                .memberId(memberId)
                .accountNumber(accountNumber)
                .balance(amount)
                .accountStatus(AccountStatus.IN_USE)
                .build();

        Transaction transaction = Transaction.builder()
                .balance(amount)
                .accountNumber(accountNumber)
                .accountStatus(TransactionStatus.CANCEL)
                .transactionResult(amount)
                .build();

        given(accountRepository.findByAccountNumber(any()))
                .willReturn(Optional.ofNullable(account));

        given(transactionRepository.findById( anyLong() ))
                .willReturn(Optional.ofNullable(transaction));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.cancel(request));
        assertEquals("?????? ?????? ???????????????.", exception.getMessage());
    }

    @Test
    @DisplayName("????????????")
    void ????????????() throws Exception {
        Long transactionId = 1L;
        Long amount = 1000L;
        Long accountId = 1L;
        String accountNumber = "1234567890";


        given(transactionRepository.findById(anyLong()))
                .willReturn(Optional.of(Transaction.builder()
                                .balance(amount)
                                .accountNumber(accountNumber)
                                .transactionResult(amount)
                                .accountId(accountId)
                                .accountStatus(TransactionStatus.PAY)
                                .build()));

        TransactionDto transactionDto = transactionService.transaction(anyLong());

        assertAll(
                () -> assertEquals(transactionDto.getAccountNumber(), accountNumber),
                () -> assertEquals(transactionDto.getTransactionStatus(), TransactionStatus.PAY),
                () -> assertEquals(transactionDto.getAmount(), amount)
        );
    }

}
