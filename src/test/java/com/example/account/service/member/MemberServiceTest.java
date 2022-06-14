package com.example.account.service.member;


import com.example.account.domain.member.Member;
import com.example.account.domain.member.MemberStatus;
import com.example.account.repository.member.MemberRepository;
import com.example.account.service.account.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

//    @Mock
//    private MemberRepository memberRepository;
//
//    @InjectMocks
//    private MemberService memberService;
//
//    @Test
//    @DisplayName("회원_가입 - 정상")
//    void 회원_가입() throws Exception {
//        Member member = Member.builder()
//                .id(1L)
//                .name("신성수")
//                .phoneNumber("01020834409")
//                .birthDay("19950918")
//                .status(MemberStatus.NORMAL)
//                .build();
//
//        given(memberRepository.findByNameAndBirthDayAndPhoneNumber(member.getName(),
//                                                                    member.getBirthDay(),
//                                                                    member.getPhoneNumber()))
//                .willReturn(Optional.empty());
//
//        memberService.createMember(member);
//    }
//
//
//    @Test
//    @DisplayName("회원_가입 - 회원이 이미 존재할때")
//    void 회원_가입_회원이_존재할때() throws Exception {
//        Member member = Member.builder()
//                .id(1L)
//                .name("신성수")
//                .phoneNumber("01020834409")
//                .birthDay("19950918")
//                .status(MemberStatus.NORMAL)
//                .build();
//
//        given(memberRepository.findByNameAndBirthDayAndPhoneNumber(member.getName(),
//                                                                    member.getBirthDay(),
//                                                                    member.getPhoneNumber()))
//                .willReturn(Optional.of(member));
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> memberService.createMember(member));
//
//        assertEquals("회원이 이미 존재합니다.", exception.getMessage());
//    }


}
