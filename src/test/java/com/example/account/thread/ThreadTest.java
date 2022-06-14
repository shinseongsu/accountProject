package com.example.account.thread;

import com.example.account.domain.account.Account;
import com.example.account.domain.account.AccountStatus;
import com.example.account.domain.member.Member;
import com.example.account.domain.member.MemberStatus;
import com.example.account.dto.transcation.DepositDto;
import com.example.account.repository.account.AccountRepository;
import com.example.account.repository.member.MemberRepository;
import com.example.account.service.member.MemberService;
import com.example.account.service.transaction.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.given;

@SpringBootTest
public class ThreadTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TransactionService transactionService;

    ExecutorService executorService = Executors.newFixedThreadPool(100);

    @Test
    void test() throws Exception {

        Member member = Member.builder()
                .name("test")
                .birthDay("2000101")
                .phoneNumber("01012345678")
                .status(MemberStatus.NORMAL)
                .accounts(List.of(Account.builder()
                        .accountNumber("0000000001")
                        .accountStatus(AccountStatus.IN_USE)
                        .balance(1000L)
                        .build()))
                .build();
        memberRepository.save(member);

        DepositDto depositDto = DepositDto.builder()
                .memberId(member.getId())
                .accountNumber("0000000001")
                .amount(200L)
                .build();

        IntStream.range(1, 100).forEach(i -> {
            executorService.execute(() -> {
                transactionService.deposit(depositDto);
            });
        });

        Thread.sleep(1000);
    }


}
