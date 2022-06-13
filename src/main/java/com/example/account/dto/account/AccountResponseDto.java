package com.example.account.dto.account;

import com.example.account.domain.account.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "계좌 정보 응답 DTO")
@Getter
@NoArgsConstructor
public class AccountResponseDto {
    @Schema(description = "결과 코드")
    private String code;
    @Schema(description = "결과 메시지")
    private String message;
    @Schema(description = "계좌 번호")
    private String accountNumber;
    @Schema(description = "잔액")
    private Long balance;
    @Schema(description = "상태")
    private AccountStatus accountStatus;

    @Builder
    public AccountResponseDto(String code, String message, String accountNumber, Long balance, AccountStatus accountStatus) {
        this.code = code;
        this.message = message;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountStatus = accountStatus;
    }

}
