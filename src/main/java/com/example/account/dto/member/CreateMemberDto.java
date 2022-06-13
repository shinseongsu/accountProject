package com.example.account.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CreateMemberDto {

    @Schema(description = "회원 가입 요청 DTO")
    @Getter @Setter
    @NoArgsConstructor
    @Builder
    public static class Request {
        @Schema(description = "이름")
        @NotEmpty
        private String name;
        @Schema(description = "핸드폰 번호")

        @NotEmpty
        private String phoneNumber;

        @NotEmpty
        @Schema(description = "생년월일")
        private String birthDay;

        @NotEmpty
        @Size(min = 4, max = 4)
        @Schema(description = "계좌 비밀번호")
        private String password;

        @Builder
        public Request(String name, String phoneNumber, String birthDay, String password) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.birthDay = birthDay;
            this.password = password;
        }
    }

    @Schema(description = "회원 가입 응답 DTO")
    @Getter @Setter
    @Builder
    public static class Response {
        @Schema(description = "결과 코드")
        private String code;
        @Schema(description = "결과 메시지")
        private String message;
        @Schema(description = "계좌번호")
        private String accountNumber;
    }

}
