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

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final AccountUtils accountUtils;

    public void unregister(UnregisterAccountDto.Request request) {
        Account account =  accountRepository.findByAccountNumber(request.getAccountNumber())
                        .orElseThrow(() -> new AccountException("계좌 정보가 없습니다."));

        if(account.getAccountStatus().equals(AccountStatus.UNREGISTERED)) {
            throw new AccountException("이미 해지 상태입니다.");
        }

        account.unregister();
    }

    public String createAccount(CreateAccountDto.Request request) {
        Member member = memberRepository.findByNameAndBirthDayAndPhoneNumber(request.getName(), request.getBirthDay(), request.getPhoneNumber())
                .orElseThrow(() -> new AccountException("가입된 유저가 아닙니다."));
        member.isAccountCountTen();

        Account account = Account.builder()
                .accountNumber(accountUtils.create())
                .balance(1000L)
                .accountStatus(AccountStatus.IN_USE)
                .memberId(member.getId())
                .password(Base64.encodeBase64String(request.getPassword().getBytes()))
                .build();
        accountRepository.save(account);

        return account.getAccountNumber();
    }

    public AccountResponseDto getAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException("가입된 유저가 아닙니다."));

        return AccountResponseDto.builder()
                .code(Code.SUCESS.getCode())
                .message(Code.SUCESS.getMessage())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .accountStatus(account.getAccountStatus())
                .build();
    }
}
