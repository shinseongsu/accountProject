package com.example.account.service.account;

import com.example.account.constatnt.Code;
import com.example.account.domain.account.Account;
import com.example.account.domain.account.AccountStatus;
import com.example.account.domain.member.Member;
import com.example.account.dto.account.AccountResponseDto;
import com.example.account.dto.account.CreateAccountDto;
import com.example.account.dto.account.UnregisterAccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.account.AccountRepository;
import com.example.account.repository.member.MemberRepository;
import com.example.account.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    public UnregisterAccountDto.Response unregister(UnregisterAccountDto unregisterAccountDto) {
        Account account =  accountRepository.findByAccountNumberAndMemberId(unregisterAccountDto.getAccountNumber(), unregisterAccountDto.getMemberId())
                        .orElseThrow(() -> new AccountException("계좌 정보가 없습니다."));

        if(account.getAccountStatus().equals(AccountStatus.UNREGISTERED)) {
            throw new AccountException("이미 해지 상태입니다.");
        }

        account.unregister();

        return UnregisterAccountDto.Response.builder()
                .code(Code.UNREGISTER_SUCCESS.getCode())
                .message(Code.UNREGISTER_SUCCESS.getMessage())
                .memberId(unregisterAccountDto.getMemberId())
                .accountNumber(unregisterAccountDto.getAccountNumber())
                .unregisterDate(account.getModifiedDate())
                .build();
    }

    public CreateAccountDto.Response createAccount(CreateAccountDto createAccountDto) {
        Member member = memberRepository.findAccountListByMemberId(createAccountDto.getMemberId())
                .orElseThrow(() -> new AccountException("가입된 유저가 아닙니다."));
        member.isAccountCountTen();

        Account account = Account.builder()
                .accountNumber(findMaxId())
                .balance(createAccountDto.getBalance())
                .accountStatus(AccountStatus.IN_USE)
                .memberId(member.getId())
                .build();
        accountRepository.save(account);

        return CreateAccountDto.Response.builder()
                .code(Code.SUCESS.getCode())
                .message(Code.SUCESS.getMessage())
                .memberId(member.getId())
                .accountNumber(account.getAccountNumber())
                .registerDate(account.getCreateDate())
                .build();
    }

    public AccountResponseDto getAccount(Long memberId) {
        Member member = memberRepository.findAccountListByMemberId(memberId)
                .orElseThrow(() -> new AccountException("가입된 유저가 아닙니다."));

        return AccountResponseDto.builder()
                .code(Code.SUCESS.getCode())
                .message(Code.SUCESS.getMessage())
                .accounts(member.getAccounts().stream().map(account ->
                                        AccountResponseDto.Accounts.builder()
                                                    .accountNumber(account.getAccountNumber())
                                                    .balance(account.getBalance())
                                                    .build()
                                        ).collect(Collectors.toList()))
                .build();
    }

    public String findMaxId() {
        Integer maxSeq = accountRepository.findMaxId()
                .orElse(0);

        return String.format("%010d", maxSeq+1);
    }

}
