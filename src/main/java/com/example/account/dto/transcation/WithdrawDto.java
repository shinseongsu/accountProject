package com.example.account.dto.transcation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class WithdrawDto {

    @Schema(description = "출금 요청 DTO")
    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        @NotEmpty
        @Schema(description = "계좌 번호")
        private String accountNumber;
        @NotEmpty
        @Schema(description = "출금 금액")
        private String amount;
        @NotEmpty
        @Schema(description = "결제 비밀번호")
        private String password;

        @Builder
        public Request(String accountNumber, String amount, String password) {
            this.accountNumber = accountNumber;
            this.amount = amount;
            this.password = password;
        }

    }

    @Schema(description = "출금 응답 DTO")
    @Getter
    @Builder
    public static class Response {
        @Schema(description = "결과 코드")
        private String code;
        @Schema(description = "결과 메시지")
        private String message;
        @Schema(description = "계좌 번호")
        private String accountNumber;
        @Schema(description = "현재 잔액")
        private Long currentBalance;
    }

}
