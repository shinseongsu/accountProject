package com.example.account.controller.member;

import com.example.account.constatnt.Code;
import com.example.account.domain.member.MemberStatus;
import com.example.account.domain.account.Account;
import com.example.account.domain.account.AccountStatus;
import com.example.account.domain.member.Member;
import com.example.account.dto.account.UnregisterAccountDto;
import com.example.account.dto.member.CreateMemberDto;
import com.example.account.service.member.MemberService;
import com.example.account.utils.AccountUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    private final AccountUtils accountUtils;

    @PostMapping(value = "/member")
    @Operation(summary = "회원 가입 API", description = "회원을 가입시킨다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CreateMemberDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERVAL SERVER ERROR")
    })
    public ResponseEntity<?> createMembeer(@RequestBody @Valid CreateMemberDto.Request request) {

        Member member = Member.builder()
                .name(request.getName())
                .birthDay(request.getBirthDay())
                .phoneNumber(request.getPhoneNumber())
                .status(MemberStatus.NORMAL)
                .accounts(List.of(Account.builder()
                                    .accountNumber(accountUtils.create())
                                    .accountStatus(AccountStatus.IN_USE)
                                    .password(Base64.encodeBase64String(request.getPassword().getBytes()))
                                    .balance(1_000L)
                                    .build()))
                .build();

        memberService.createMember(member);

        return ResponseEntity.ok(CreateMemberDto.Response.builder()
                .code(Code.SUCESS.getCode())
                .message(Code.SUCESS.getMessage())
                .accountNumber(member.getAccounts().get(0).getAccountNumber())
                .build());
    }

}
