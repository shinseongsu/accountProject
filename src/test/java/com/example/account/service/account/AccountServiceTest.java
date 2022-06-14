package com.example.account.service.account;

import com.example.account.constatnt.Code;
import com.example.account.domain.account.Account;
import com.example.account.domain.account.AccountStatus;
import com.example.account.domain.member.Member;
import com.example.account.domain.member.MemberStatus;
import com.example.account.domain.transaction.TransactionStatus;
import com.example.account.dto.account.AccountResponseDto;
import com.example.account.dto.account.CreateAccountDto;
import com.example.account.dto.account.UnregisterAccountDto;
import com.example.account.repository.account.AccountRepository;
import com.example.account.repository.member.MemberRepository;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private AccountService accountService;

    @Test
    @DisplayName("계좌 해지 테스트 - 정상")
    void 계좌_해지_TEST() throws Exception {
        String accountNumber = "testAccountNumber";
        UnregisterAccountDto request = UnregisterAccountDto.builder()
                        .accountNumber(accountNumber)
                        .build();

        given(accountRepository.findByAccountNumberAndMemberId(any(), any()))
                .willReturn(Optional.of(Account.builder()
                        .id(1L)
                        .accountNumber(accountNumber)
                        .accountStatus(AccountStatus.IN_USE)
                        .balance(100L)
                        .build()));

        accountService.unregister(request);
    }

    @Test
    @DisplayName("계좌 해지 테스트 - 계좌 정보가 없을 떄,")
    void 계좌_해지_테스트_계좌_정보가_없을때_TEST() throws Exception {
        UnregisterAccountDto request = new UnregisterAccountDto();

        given(accountRepository.findByAccountNumber(any())).willReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> accountService.unregister(request));

        assertEquals("계좌 정보가 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("계좌 해지 테스트 - 이미 해지된 상태")
    void 계좌_해지_테스트_해지된_상태_TEST() throws Exception {
        UnregisterAccountDto request = new UnregisterAccountDto();

        given(accountRepository.findByAccountNumberAndMemberId(any(), any())).willReturn(
                        Optional.of(Account.builder()
                        .accountStatus(AccountStatus.UNREGISTERED)
                        .build()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> accountService.unregister(request));

        assertEquals("이미 해지 상태입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("계좌 만들기 - 정상")
    void 계좌_만들기() throws Exception {
        Long memberId = 1L;
        Long balance = 1000L;
        String testAccountNumber = "0000000001";

        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .memberId(memberId)
                .balance(balance)
                .build();

        given(accountRepository.findMaxId()).willReturn(Optional.empty());
        given(memberRepository.findAccountListByMemberId(memberId))
                .willReturn(Optional.of(Member.builder()
                                .id(1L)
                                .accounts(List.of(Account.builder().id(1L).build()))
                                .build()));

        CreateAccountDto.Response response = accountService.createAccount(createAccountDto);

        assertAll(
                () -> assertEquals(response.getCode(), Code.SUCESS.getCode()),
                () -> assertEquals(response.getMessage(), Code.SUCESS.getMessage()),
                () -> assertEquals(response.getAccountNumber(), testAccountNumber),
                () -> assertEquals(response.getMemberId(), 1L)
        );
    }

    @Test
    @DisplayName("계좌 만들기 - 가입된 유저가 아닙니다.")
    void 계좌_개설_가입된_유저가_아닙니다_TEST() throws Exception {
        Long memberId = 1L;
        Long balance = 1000L;

        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .memberId(memberId)
                .balance(balance)
                .build();

        given(memberRepository.findByNameAndBirthDayAndPhoneNumber(anyString(), anyString(), anyString()))
                .willReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> accountService.createAccount(createAccountDto));

        assertEquals("가입된 유저가 아닙니다.", exception.getMessage());
    }


    @Test
    @DisplayName("계좌 만들기 - 10개가 넘는 계좌")
    void 계좌_만들기_계좌_10개_이상_TEST() throws Exception {
        Long memberId = 1L;
        Long balance = 1000L;

        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .memberId(memberId)
                .balance(balance)
                .build();

        List<Account> list = new ArrayList();
        IntStream.range(1, 15).forEach(i ->
                list.add(Account.builder().id((long) i).build())
        );

        given(memberRepository.findAccountListByMemberId(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(1L)
                        .name("test")
                        .phoneNumber("01012345678")
                        .birthDay("20000101")
                        .accounts(list)
                        .build()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> accountService.createAccount(createAccountDto));

        assertEquals("계좌는 10개까지 생성가능합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("계좌 조회 - 정상")
    void 계좌_조회() throws Exception {
        String accountNumber = "1234567890";
        Long balance = 1000L;

        given(memberRepository.findAccountListByMemberId(any()))
                .willReturn(Optional.of(Member.builder()
                        .name("test")
                        .status(MemberStatus.NORMAL)
                                .accounts(List.of(Account.builder()
                                                .accountNumber(accountNumber)
                                                .balance(balance)
                                                .build()))
                        .build()));

        AccountResponseDto accountResponse = accountService.getAccount(anyLong());

        assertAll(
                () -> assertEquals(accountResponse.getCode(), Code.SUCESS.getCode()),
                () -> assertEquals(accountResponse.getMessage(), Code.SUCESS.getMessage()),
                () -> assertEquals(accountResponse.getAccounts().get(0).getAccountNumber(), accountNumber),
                () -> assertEquals(accountResponse.getAccounts().get(0).getBalance(), balance)
        );
    }


    @Test
    @DisplayName("계좌 조회 - 가입된 유저가 아닙니다.")
    void 계좌_조회_가입된_유저가_아닙니다_TEST() throws Exception {
        Long memberId = 1L;

        given(memberRepository.findAccountListByMemberId(memberId))
                .willReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> accountService.getAccount(memberId));

        assertEquals("가입된 유저가 아닙니다.", exception.getMessage());
    }

}
