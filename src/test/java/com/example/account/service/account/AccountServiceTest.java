package com.example.account.service.account;

import com.example.account.constatnt.Code;
import com.example.account.domain.account.Account;
import com.example.account.domain.account.AccountStatus;
import com.example.account.domain.member.Member;
import com.example.account.dto.account.AccountResponseDto;
import com.example.account.dto.account.CreateAccountDto;
import com.example.account.dto.account.UnregisterAccountDto;
import com.example.account.repository.account.AccountRepository;
import com.example.account.repository.member.MemberRepository;
import com.example.account.utils.AccountUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

//    @Mock
//    private AccountRepository accountRepository;
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @Mock
//    private AccountUtils accountUtils;
//
//    @InjectMocks
//    private AccountService accountService;
//
//    @Test
//    @DisplayName("계좌 해지 테스트 - 정상")
//    void 계좌_해지_TEST() throws Exception {
//        String accountNumber = "testAccountNumber";
//        UnregisterAccountDto.Request request = UnregisterAccountDto.Request.builder()
//                        .accountNumber(accountNumber)
//                        .build();
//
//        given(accountRepository.findByAccountNumber(any())).willReturn(Optional.of(Account.builder()
//                        .id(1L)
//                        .accountNumber(accountNumber)
//                        .accountStatus(AccountStatus.IN_USE)
//                        .password("1111")
//                        .balance(100L)
//                        .build()));
//
//        accountService.unregister(request);
//    }
//
//    @Test
//    @DisplayName("계좌 해지 테스트 - 계좌 정보가 없을 떄,")
//    void 계좌_해지_테스트_계좌_정보가_없을때_TEST() throws Exception {
//        UnregisterAccountDto.Request request = new UnregisterAccountDto.Request();
//
//        given(accountRepository.findByAccountNumber(any())).willReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> accountService.unregister(request));
//
//        assertEquals("계좌 정보가 없습니다.", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("계좌 해지 테스트 - 이미 해지된 상태")
//    void 계좌_해지_테스트_해지된_상태_TEST() throws Exception {
//        UnregisterAccountDto.Request request = new UnregisterAccountDto.Request();
//
//        given(accountRepository.findByAccountNumber(any())).willReturn(Optional.of(Account.builder()
//                        .accountStatus(AccountStatus.UNREGISTERED)
//                        .build()));
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> accountService.unregister(request));
//
//        assertEquals("이미 해지 상태입니다.", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("계좌 만들기 - 정상")
//    void 계좌_만들기() throws Exception {
//        String testAccountNumber = "testAccount";
//        CreateAccountDto.Request request = CreateAccountDto.Request.builder()
//                .name("신성수")
//                .phoneNumber("01020834409")
//                .birthDay("19950918")
//                .password("1111")
//                .build();
//
//        given(accountUtils.create()).willReturn(testAccountNumber);
//        given(memberRepository.findByNameAndBirthDayAndPhoneNumber(request.getName(), request.getBirthDay(), request.getPhoneNumber()))
//                .willReturn(Optional.of(Member.builder()
//                                .id(1L)
//                                .accounts(List.of(Account.builder().id(1L).build()))
//                                .build()));
//
//        String accountNumber = accountService.createAccount(request);
//        assertEquals(accountNumber, testAccountNumber);
//    }
//
//    @Test
//    @DisplayName("계좌 만들기 - 가입된 유저가 아닙니다.")
//    void 계좌_개설_가입된_유저가_아닙니다_TEST() throws Exception {
//        CreateAccountDto.Request request = CreateAccountDto.Request.builder()
//                .name("신성수")
//                .phoneNumber("01020834409")
//                .birthDay("19950918")
//                .password("1111")
//                .build();
//
//        given(memberRepository.findByNameAndBirthDayAndPhoneNumber(request.getName(), request.getBirthDay(), request.getPhoneNumber()))
//                .willReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> accountService.createAccount(request));
//
//        assertEquals("가입된 유저가 아닙니다.", exception.getMessage());
//    }
//
//
//    @Test
//    @DisplayName("계좌 만들기 - 10개가 넘는 계좌")
//    void 계좌_만들기_계좌_10개_이상_TEST() throws Exception {
//        CreateAccountDto.Request request = CreateAccountDto.Request.builder()
//                .name("신성수")
//                .phoneNumber("01020834409")
//                .birthDay("19950918")
//                .password("1111")
//                .build();
//
//        List<Account> list = new ArrayList();
//        IntStream.range(1, 15).forEach( i ->
//                list.add(Account.builder().id((long) i).build())
//        );
//
//        given(memberRepository.findByNameAndBirthDayAndPhoneNumber(request.getName(), request.getBirthDay(), request.getPhoneNumber()))
//                .willReturn(Optional.of(Member.builder()
//                        .id(1L)
//                        .name(request.getName())
//                        .phoneNumber(request.getPhoneNumber())
//                        .birthDay(request.getBirthDay())
//                        .accounts(list)
//                        .build()));
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> accountService.createAccount(request));
//
//        assertEquals("계좌는 10개까지 생성가능합니다.", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("계좌 조회 - 정상")
//    void 계좌_조회() throws Exception {
//        String accountNumber = "testAccountNumber";
//        Long balance = 1000L;
//        AccountStatus status = AccountStatus.IN_USE;
//
//        given(accountRepository.findByAccountNumber(accountNumber))
//                .willReturn(Optional.of(Account.builder()
//                        .accountNumber(accountNumber)
//                        .balance(balance)
//                        .accountStatus(status)
//                        .build()));
//
//        AccountResponseDto accountResponse = accountService.getAccount(accountNumber);
//
//        assertAll(
//                () -> assertEquals(accountResponse.getCode(), Code.SUCESS.getCode()),
//                () -> assertEquals(accountResponse.getMessage(), Code.SUCESS.getMessage()),
//                () -> assertEquals(accountResponse.getAccountNumber(), accountNumber),
//                () -> assertEquals(accountResponse.getBalance(), balance),
//                () -> assertEquals(accountResponse.getAccountStatus(), status)
//        );
//    }
//
//
//    @Test
//    @DisplayName("계좌 조회 - 가입된 유저가 아닙니다.")
//    void 계좌_조회_가입된_유저가_아닙니다_TEST() throws Exception {
//        String accountNumber = "testAccountNumber";
//
//        given(accountRepository.findByAccountNumber(accountNumber))
//                .willReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> accountService.getAccount(accountNumber));
//
//        assertEquals("가입된 유저가 아닙니다.", exception.getMessage());
//    }

}
