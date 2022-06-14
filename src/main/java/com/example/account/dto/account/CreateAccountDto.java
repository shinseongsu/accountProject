package com.example.account.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "계좌 생성 요청 DTO")
@Getter @Setter
@NoArgsConstructor
public class CreateAccountDto {

    @NotNull
    @Schema(description = "회원 아이디")
    private Long memberId;

    @NotNull
    @Schema(description = "초기 잔액")
    private Long balance;

    @Builder
    public CreateAccountDto(Long memberId, Long balance) {
        this.memberId = memberId;
        this.balance = balance;
    }

    @Schema(description = "계좌 생성 응답 DTO")
    @Getter
    public static class Response {
        @Schema(description = "결과 코드")
        private String code;
        @Schema(description = "결과 메시지")
        private String message;
        @Schema(description = "회원 아이디")
        private Long memberId;
        @Schema(description = "생성된 계좌 번호")
        private String accountNumber;
        @Schema(description = "등록일시")
        private LocalDateTime registerDate;

        @Builder
        public Response(String code, String message, Long memberId, String accountNumber, LocalDateTime registerDate) {
            this.code = code;
            this.message = message;
            this.memberId = memberId;
            this.accountNumber = accountNumber;
            this.registerDate = registerDate;
        }

    }
}
