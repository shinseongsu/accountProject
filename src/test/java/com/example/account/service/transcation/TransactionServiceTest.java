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
    @DisplayName("계좌 입금")
    void 계좌_입금_TEST() throws Exception {
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
    @DisplayName("계좌 입금 - 계좌가 존재하지 않을 때,")
    void 계좌_입금_계좌가_존재하지_않을때_TEST() throws Exception {
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

        assertEquals("계좌가 존재하지 않습니다.", exception.getMessage());
    }


    @Test
    @DisplayName("계좌 출금")
    void 계좌_출금_TEST() throws Exception {
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
    @DisplayName("계좌 출금시, 계좌가 존재하지 않을 때")
    void 계좌_출금시_계좌가_존재하지_않을때() throws Exception {
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
        assertEquals("계좌가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("결제 테스트")
    void 결졔_테스트() throws Exception {
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
    @DisplayName("[결제] 계좌가 존재하지 않을때,")
    void 결제_계좌가_존재하지_않을때() throws Exception {
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
        assertEquals("계좌가 존재하지 않습니다.", exception.getMessage());
    }


    @Test
    @DisplayName("결제 취소")
    void 결제_취소_TEST() throws Exception {
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
    @DisplayName("[결제 취소] 상태가 결제가 아닐 경우")
    void 결제_취소_상태가_결제가_아닐경우() throws Exception {
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
        assertEquals("결제만 취소 가능합니다.", exception.getMessage());
    }


    @Test
    @DisplayName("[결제 취소] 계좌가 존재하지 않을 때,")
    void 결제_취소_계좌가_존재하지_않을때() throws Exception {
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
        assertEquals("계좌가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("[결제 취소] 거래 정보가 존재하지 않을떄")
    void 결제_취소_거래_정보가_존재하지_않을때() throws Exception {
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
        assertEquals("거래 정보가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("[결제 취소] 이미 취소 상태일때,")
    void 결제_취소_이미_취소_상태일때() throws Exception {
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
        assertEquals("이미 취소 상태입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("거래조회")
    void 거래조히() throws Exception {
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
