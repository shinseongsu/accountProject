package com.example.account.dto.transcation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class PayDto {

    @Schema(description = "결제 요청 Dto")
    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        @NotEmpty
        @Schema(description = "계좌 번호")
        private String accountNumber;
        @NotEmpty
        @Schema(description = "상품명")
        private String orderName;
        @NotEmpty
        @Schema(description = "결제 금액")
        private Long amount;
        @NotEmpty
        @Schema(description = "계좌 비밀번호")
        private String password;
    }

    @Schema(description = "계좌 번호")
    @Getter
    @Builder
    public static class Response {
        @Schema(description = "주문 아이디")
        private Long orderId;
        @Schema(description = "결과 코드")
        private String code;
        @Schema(description = "결과 메시지")
        private String message;
        @Schema(description = "상품명")
        private String orderName;
        @Schema(description = "결제 금액")
        private Long balance;
        @Schema(description = "현재 계좌 잔액")
        private Long accountBalance;
    }

}
