package com.example.account.dto.transcation;

import com.example.account.domain.transaction.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "거래 내역 응답 Dto")
@Getter
@NoArgsConstructor
public class TransactionDto {
    @Schema(description = "결과 코드")
    private String code;
    @Schema(description = "결과 메시지")
    private String message;
    @Schema(description = "계좌 번호")
    private String accountNumber;
    private TransactionStatus transactionStatus;
    private Long transactionResult;
    private Long transactionId;
    private Long amount;
    private LocalDateTime transactionDate;

    @Builder
    public TransactionDto(String code, String message, String accountNumber, TransactionStatus transactionStatus,
                          Long transactionResult, Long transactionId, Long amount, LocalDateTime transactionDate) {
        this.code = code;
        this.message = message;
        this.accountNumber = accountNumber;
        this.transactionStatus = transactionStatus;
        this.transactionResult = transactionResult;
        this.transactionId = transactionId;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

}
