package com.example.account.dto.transcation;

import com.example.account.domain.transaction.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "거래 상세 내역")
public class TransactionList {
    @Schema(description = "주문 아이디")
    private Long orderId;
    @Schema(description = "상품명")
    private String orderName;
    @Schema(description = "거래 금액")
    private Long balance;
    @Schema(description = "거래 상태")
    private TransactionStatus status;
    @Schema(description = "거래 일자")
    private LocalDateTime transactionDate;

    @Builder
    public TransactionList(Long orderId, String orderName, Long balance, TransactionStatus status, LocalDateTime transactionDate) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.balance = balance;
        this.status = status;
        this.transactionDate = transactionDate;
    }

}
