package com.example.account.dto.transcation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "취소 요청 DTO")
@Getter @Setter
@NoArgsConstructor
public class CancelDto {

    @NotNull
    @Schema(description = "거래 번호")
    private Long transactionId;

    @NotEmpty
    @Schema(description = "계좌 번호")
    private String accountNumber;

    @NotNull
    @Schema(description = "거래 금액")
    private Long amount;

    @Builder
    public CancelDto(Long transactionId, String accountNumber, Long amount) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    @Schema(description = "취소 응답 DTO")
    @Getter
    public static class Response {
        @Schema(description = "결과 코드")
        private String code;
        @Schema(description = "결과 메시지")
        private String message;
        @Schema(description = "계좌 번호")
        private String accountNumber;
        @Schema(description = "계좌 현재 잔액")
        private Long transactionResult;
        @Schema(description = "거래 아이디")
        private Long transactionId;
        @Schema(description = "취소 거래 금액")
        private Long amount;
        @Schema(description = "거래 일시")
        private LocalDateTime transactionDate;

        @Builder
        public Response(String code, String message, String accountNumber, Long transactionResult,
                        Long transactionId, Long amount, LocalDateTime transactionDate) {
            this.code = code;
            this.message = message;
            this.accountNumber = accountNumber;
            this.transactionResult = transactionResult;
            this.transactionId = transactionId;
            this.amount = amount;
            this.transactionDate = transactionDate;
        }
    }

}
