package com.example.account.dto.account;

import com.example.account.constatnt.Code;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "계좌 해지 요청 DTO")
@Getter @Setter
@NoArgsConstructor
public class UnregisterAccountDto {

    @Schema(description = "회원 아이디")
    @NotNull
    private Long memberId;

    @Schema(description = "계좌 번호")
    @NotEmpty
    private String accountNumber;

    @Builder
    public UnregisterAccountDto(Long memberId, String accountNumber) {
        this.memberId = memberId;
        this.accountNumber = accountNumber;
    }


    @Schema(description = "계좌 해지 응답 DTO")
    @Getter @Setter
    @Builder
    public static class Response {
        @Schema(description = "결과 코드")
        private String code;
        @Schema(description = "결과 메시지")
        private String message;
        @Schema(description = "회원 아이디")
        private Long memberId;
        @Schema(description = "계좌 번호")
        private String accountNumber;
        @Schema(description = "해지일시")
        private LocalDateTime unregisterDate;

        @Builder
        public Response(String code, String message, Long memberId, String accountNumber, LocalDateTime unregisterDate) {
            this.code = code;
            this.message = message;
            this.memberId = memberId;
            this.accountNumber = accountNumber;
            this.unregisterDate = unregisterDate;
        }

    }

}
