package com.example.account.dto.transcation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Schema(description = "전체 페이지")
    private Integer totalPage;
    @Schema(description = "거래 상세 내역")
    private List<TransactionList> transactionList;

    @Builder
    public TransactionDto(String code, String message, String accountNumber, Integer totalPage,
                          List<TransactionList> transactionList) {
        this.code = code;
        this.message = message;
        this.accountNumber = accountNumber;
        this.totalPage = totalPage;
        this.transactionList = transactionList;
    }

}
