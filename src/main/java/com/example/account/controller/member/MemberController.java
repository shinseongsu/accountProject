package com.example.account.controller.member;

import com.example.account.constatnt.Code;
import com.example.account.domain.member.MemberStatus;
import com.example.account.domain.account.Account;
import com.example.account.domain.account.AccountStatus;
import com.example.account.domain.member.Member;
import com.example.account.dto.account.UnregisterAccountDto;
import com.example.account.dto.member.CreateMemberDto;
import com.example.account.service.account.AccountService;
import com.example.account.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AccountService accountService;

    @PostMapping(value = "/member")
    @Operation(summary = "회원 가입 API", description = "회원을 가입시킨다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CreateMemberDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERVAL SERVER ERROR")
    })
    public ResponseEntity<CreateMemberDto.Response> createMembeer(@RequestBody @Valid CreateMemberDto createMemberDto) {

        Member member = Member.builder()
                .name(createMemberDto.getName())
                .birthDay(createMemberDto.getBirthDay())
                .phoneNumber(createMemberDto.getPhoneNumber())
                .status(MemberStatus.NORMAL)
                .accounts(List.of(Account.builder()
                                    .accountNumber(accountService.findMaxId())
                                    .accountStatus(AccountStatus.IN_USE)
                                    .balance(createMemberDto.getBalance())
                                    .build()))
                .build();

        memberService.createMember(member);

        return ResponseEntity.ok(CreateMemberDto.Response.builder()
                .code(Code.SUCESS.getCode())
                .message(Code.SUCESS.getMessage())
                .accountNumber(member.getAccounts().get(0).getAccountNumber())
                .memberId(member.getId())
                .build());
    }

}
