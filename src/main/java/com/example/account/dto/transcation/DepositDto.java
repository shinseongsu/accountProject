package com.example.account.dto.transcation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class DepositDto {

    @Schema(description = "입금 요청 DTO")
    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        @NotEmpty
        @Schema(description = "계좌 번호")
        private String accountNumber;
        @NotEmpty
        @Schema(description = "입금 금액")
        private Long amount;
    }

    @Schema(description = "입금 응답 DTO")
    @Getter
    @Builder
    public static class Response {
        @Schema(description = "결과 코드")
        private String code;
        @Schema(description = "결과 메시지")
        private String message;
        @Schema(description = "계좌 번호")
        private String accountNumber;
        @Schema(description = "계좌 현재 잔액")
        private Long currentBalance;
    }

}
