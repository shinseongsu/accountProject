package com.example.account.dto.transcation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class CancelDto {

    @Schema(description = "취소 요청 DTO")
    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        @NotEmpty
        @Schema(description = "주문 아이디")
        private String orderId;
        @NotEmpty
        @Schema(description = "계좌 번호")
        private String accountNumber;
        @NotEmpty
        @Schema(description = "계좌 비밀번호")
        private String password;

        @Builder
        public Request(String orderId, String accountNumber, String password) {
            this.orderId = orderId;
            this.accountNumber = accountNumber;
            this.password = password;
        }

    }

    @Schema(description = "취소 응답 DTO")
    @Getter
    @Builder
    public static class Response {
        @Schema(description = "결과 코드")
        private String code;
        @Schema(description = "결과 메시지")
        private String message;
        @Schema(description = "주문 아이디")
        private Long orderId;
        @Schema(description = "상품명")
        private String orderName;
        @Schema(description = "취소 금액")
        private Long balance;
        @Schema(description = "계좌 현재 잔액")
        private Long accountBalance;
    }

}
