package com.example.account.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "회원 가입 요청 DTO")
@Getter @Setter
@NoArgsConstructor
public class CreateMemberDto {

    @Schema(description = "이름")
    @NotEmpty
    private String name;

    @Schema(description = "핸드폰 번호")
    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    @Schema(description = "생년월일")
    private String birthDay;

    @NotNull
    @Schema(description = "초기 금액")
    private Long balance;

    @Builder
    public CreateMemberDto(String name, String phoneNumber, String birthDay, Long balance) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.balance = balance;
    }

    @Schema(description = "회원 가입 응답 DTO")
    @Getter
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "결과 코드")
        private String code;
        @Schema(description = "결과 메시지")
        private String message;
        @Schema(description = "계좌번호")
        private String accountNumber;
        @Schema(description = "사용자 아이디")
        private Long memberId;

        @Builder
        public Response(String code, String message, String accountNumber, Long memberId) {
            this.code = code;
            this.message = message;
            this.accountNumber = accountNumber;
            this.memberId = memberId;
        }
    }

}
