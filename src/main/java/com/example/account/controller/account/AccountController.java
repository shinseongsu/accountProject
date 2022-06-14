package com.example.account.controller.account;

import com.example.account.constatnt.Code;
import com.example.account.dto.account.AccountResponseDto;
import com.example.account.dto.account.CreateAccountDto;
import com.example.account.dto.account.UnregisterAccountDto;
import com.example.account.service.account.AccountService;
import com.example.account.service.RedisTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final RedisTestService redisTestService;

    @GetMapping("/get-lock")
    @Operation(summary = "레디스 락", description = "레디스 db를 lock 한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "INTERVAL SERVER ERROR")
    })
    public String getLock() {
        return redisTestService.getLock();
    }

    @PutMapping("/account")
    @Operation(summary = "계좌 정지", description = "계좌 번호를 정지합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UnregisterAccountDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERVAL SERVER ERROR")
    })
    public ResponseEntity<UnregisterAccountDto.Response> unregistered(@RequestBody @Valid UnregisterAccountDto unregisterAccountDto) {
        return ResponseEntity.ok(accountService.unregister(unregisterAccountDto));
    }

    @PostMapping("/account")
    @Operation(summary = "계좌 생성", description = "계좌 생성 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CreateAccountDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERVAL SERVER ERROR")
    })
    public ResponseEntity<?> createAccount(@RequestBody @Valid CreateAccountDto createAccountDto) {
        return ResponseEntity.ok(accountService.createAccount(createAccountDto));
    }

    @GetMapping("/account/{memberId}")
    @Operation(summary = "계좌 조회", description = "계좌 조회 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AccountResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERVAL SERVER ERROR")
    })
    public ResponseEntity<AccountResponseDto> getAccount(@PathVariable(value = "memberId") Long memberId) {
        return ResponseEntity.ok(accountService.getAccount(memberId));
    }

}
