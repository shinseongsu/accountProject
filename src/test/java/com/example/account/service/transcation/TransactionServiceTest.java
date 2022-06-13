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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("계좌 입금")
    void 계좌_입금_TEST() throws Exception {
        DepositDto.Request request = DepositDto.Request.builder()
                .accountNumber("testAccountNumber")
                .amount("1000")
                .build();

        Account account = Account.builder()
                .id(1L)
                .accountNumber("testAccountNumber")
                .password("1111")
                .balance(1000L)
                .accountStatus(AccountStatus.IN_USE)
                        .build();

        given(accountRepository.findByAccountNumber(request.getAccountNumber()))
                .willReturn(Optional.ofNullable(account));

        Account returnAccount = transactionService.deposit(request);
        account.deposit(Long.valueOf(request.getAmount()));

        assertAll(
                () -> assertEquals(returnAccount.getAccountNumber(), account.getAccountNumber()),
                () -> assertEquals(returnAccount.getAccountStatus(), account.getAccountStatus()),
                () -> assertEquals(returnAccount.getBalance(), account.getBalance())
        );
    }

    @Test
    @DisplayName("계좌 입금 - 계좌가 존재하지 않을 때,")
    void 계좌_입금_계좌가_존재하지_않을때_TEST() throws Exception {
        DepositDto.Request request = DepositDto.Request.builder()
                .accountNumber("testAccountNumber")
                .amount("1000")
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.deposit(request));

        assertEquals("계좌가 존재하지 않습니다.", exception.getMessage());
    }


    @Test
    @DisplayName("계좌 출금")
    void 계좌_출금_TEST() throws Exception {
        Long balance = 10000L;
        String amount = "1000";

        String accountNumber = "testAccountNumber";
        WithdrawDto.Request request = WithdrawDto.Request.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .password("1111")
                .build();

        given(accountRepository.findByAccountNumber(accountNumber))
                .willReturn(Optional.ofNullable(Account.builder()
                        .id(1L)
                        .accountNumber(accountNumber)
                        .password(Base64.encodeBase64String("1111".getBytes()))
                        .balance(balance)
                        .build()));

        Account account = transactionService.withdraw(request);

        assertEquals(account.getBalance(), balance - Long.valueOf(amount));
    }

    @Test
    @DisplayName("계좌 출금시, 계좌가 존재하지 않을 때")
    void 계좌_출금시_계좌가_존재하지_않을때() throws Exception {
        Long balance = 10000L;
        String amount = "1000";
        String accountNumber = "testAccountNumber";
        WithdrawDto.Request request = WithdrawDto.Request.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .password("1111")
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.withdraw(request));
        assertEquals("계좌가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("계좌 출금시, 패스워드가 맞지 않습니다.")
    void 계좌_출금시_패스워드가_맞지_않습니다() throws Exception {
        Long balance = 10000L;
        String amount = "1000";
        String accountNumber = "testAccountNumber";
        WithdrawDto.Request request = WithdrawDto.Request.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .password("1111")
                .build();

        given(accountRepository.findByAccountNumber(accountNumber))
                .willReturn(Optional.ofNullable(Account.builder()
                        .id(1L)
                        .accountNumber(accountNumber)
                        .password(Base64.encodeBase64String("1234".getBytes()))
                        .balance(balance)
                        .build()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.withdraw(request));
        assertEquals("패스워드가 맞지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("결제 테스트")
    void 결졔_테스트() throws Exception {
        String accountNumber = "testAccountNumber";
        String orderName = "테스트 상품";
        String amount = "1000";
        String password = "1111";

        PayDto.Request request = PayDto.Request.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .orderName(orderName)
                .password(password)
                .build();

        given(accountRepository.findByAccountNumber(accountNumber))
                .willReturn(Optional.ofNullable(Account.builder()
                        .accountNumber(accountNumber)
                        .password(Base64.encodeBase64String(password.getBytes()))
                        .balance(Long.valueOf(amount))
                        .build()));

        PayDto.Response response = transactionService.pay(request);

        assertEquals(response.getOrderName(), orderName);
        assertEquals(response.getBalance(), Long.valueOf(amount));
        assertEquals(response.getAccountBalance(), 0L);
    }

    @Test
    @DisplayName("[결제] 계좌가 존재하지 않을때,")
    void 결제_계좌가_존재하지_않을때() throws Exception {
        String accountNumber = "testAccountNumber";
        String orderName = "테스트 상품";
        String amount = "1000";
        String password = "1111";

        PayDto.Request request = PayDto.Request.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .orderName(orderName)
                .password(password)
                .build();

        given(accountRepository.findByAccountNumber(accountNumber))
                .willReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.pay(request));
        assertEquals("계좌가 존재하지 않습니다.", exception.getMessage());
    }


    @Test
    @DisplayName("[결제] 패스워드가 맞지 않을 때")
    void 결제_패스워드가_맞지_않을때() throws Exception {
        String accountNumber = "testAccountNumber";
        String orderName = "테스트 상품";
        String amount = "1000";
        String password = "1111";

        PayDto.Request request = PayDto.Request.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .orderName(orderName)
                .password(password)
                .build();

        given(accountRepository.findByAccountNumber(accountNumber))
                .willReturn(Optional.of(Account.builder().password("1234").build()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.pay(request));
        assertEquals("패스워드가 맞지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("결제 취소")
    void 결제_취소_TEST() throws Exception {
        CancelDto.Request request = CancelDto.Request.builder()
                .orderId("1")
                .accountNumber("testAccountNumber")
                .password("1111")
                .build();

        given(accountRepository.findByAccountNumber(request.getAccountNumber()))
                .willReturn(Optional.ofNullable(Account.builder()
                        .password(Base64.encodeBase64String("1111".getBytes()))
                        .build()));
        given(transactionRepository.findById( Long.valueOf(request.getOrderId())))
                .willReturn(Optional.ofNullable(Transaction.builder().id(1L).accountStatus(TransactionStatus.PAY).build()));

        CancelDto.Response response = transactionService.cancel(request);

        assertEquals(response.getOrderId(), 1L);
    }

    @Test
    @DisplayName("[결제 취소] 상태가 결제가 아닐 경우")
    void 결제_취소_상태가_결제가_아닐경우() throws Exception {
        CancelDto.Request request = CancelDto.Request.builder()
                .orderId("1")
                .accountNumber("testAccountNumber")
                .password("1111")
                .build();

        given(accountRepository.findByAccountNumber(request.getAccountNumber()))
                .willReturn(Optional.ofNullable(Account.builder()
                        .password(Base64.encodeBase64String("1111".getBytes()))
                        .build()));
        given(transactionRepository.findById( Long.valueOf(request.getOrderId())))
                .willReturn(Optional.ofNullable(Transaction.builder().id(1L).accountStatus(TransactionStatus.DEPOSIT).build()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.cancel(request));
        assertEquals("결제만 취소 가능합니다.", exception.getMessage());
    }


    @Test
    @DisplayName("[결제 취소] 계좌가 존재하지 않을 때,")
    void 결제_취소_계좌가_존재하지_않을때() throws Exception {
        CancelDto.Request request = CancelDto.Request.builder()
                .orderId("1")
                .accountNumber("testAccountNumber")
                .password("1111")
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
        CancelDto.Request request = CancelDto.Request.builder()
                .orderId("1")
                .accountNumber("testAccountNumber")
                .password("1111")
                .build();

        given(accountRepository.findByAccountNumber(request.getAccountNumber()))
                .willReturn(Optional.ofNullable(Account.builder()
                        .password(Base64.encodeBase64String("1111".getBytes()))
                        .build()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.cancel(request));
        assertEquals("거래 정보가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("[결제 취소] 이미 취소 상태일때,")
    void 결제_취소_이미_취소_상태일때() throws Exception {
        CancelDto.Request request = CancelDto.Request.builder()
                .orderId("1")
                .accountNumber("testAccountNumber")
                .password("1111")
                .build();

        given(accountRepository.findByAccountNumber(request.getAccountNumber()))
                .willReturn(Optional.ofNullable(Account.builder()
                        .password(Base64.encodeBase64String("1111".getBytes()))
                        .build()));
        given(transactionRepository.findById( Long.valueOf(request.getOrderId())))
                .willReturn(Optional.ofNullable(Transaction.builder().id(1L).accountStatus(TransactionStatus.CANCEL).build()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.cancel(request));
        assertEquals("이미 취소 상태입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("거래조회")
    void 거래조히() throws Exception {
        String accountNumber = "testAccountNumber";

        TransactionList transactionList = TransactionList.builder()
                .orderId(1L)
                .orderName("테스트 상품명")
                .balance(1000L)
                .status(TransactionStatus.DEPOSIT)
                .transactionDate(LocalDateTime.now())
                .build();

        given(accountRepository.findByAccountNumber(accountNumber))
                .willReturn(Optional.of(Account.builder().id(1L).build()));

        given(transactionRepository.findByAccountNumber(accountNumber, Pageable.ofSize(10)))
                .willReturn(new PageImpl<>(List.of(transactionList)));

        TransactionDto transactionDto = transactionService.transaction(accountNumber, Pageable.ofSize(10));
        TransactionList transactionList1 = transactionDto.getTransactionList().get(0);

        assertEquals(transactionDto.getTransactionList().size(), 1);
        assertAll(
                () -> assertEquals(transactionList1.getBalance(), 1000L),
                () -> assertEquals(transactionList1.getOrderName(), "테스트 상품명"),
                () -> assertEquals(transactionList1.getStatus(), TransactionStatus.DEPOSIT)
        );
    }

}
