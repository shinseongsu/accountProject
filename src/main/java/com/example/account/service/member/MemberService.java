package com.example.account.service.member;

import com.example.account.domain.member.Member;
import com.example.account.exception.AccountException;
import com.example.account.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public void createMember(Member member) {
        Optional<Member> isMember = memberRepository.findByNameAndBirthDayAndPhoneNumber(member.getName(),
                                                            member.getBirthDay(),
                                                            member.getPhoneNumber());

        if(isMember.isPresent()) {
            throw new AccountException("회원이 이미 존재합니다.");
        }
        memberRepository.save(member);
    }

}
