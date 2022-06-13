package com.example.account.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CreateAccountDto {

    @Schema(description = "계좌 생성 요청 DTO")
    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        @NotEmpty
        @Schema(description = "이름")
        private String name;

        @NotEmpty
        @Schema(description = "생년월일")
        private String birthDay;

        @NotEmpty
        @Schema(description = "핸드폰 번호")
        private String phoneNumber;

        @NotEmpty
        @Size(min = 4, max = 4)
        @Schema(description = "계좌 비밀번호")
        private String password;

        @Builder
        public Request(String name, String birthDay, String phoneNumber, String password) {
            this.name = name;
            this.birthDay = birthDay;
            this.phoneNumber = phoneNumber;
            this.password = password;
        }

    }

    @Schema(description = "계좌 생성 응답 DTO")
    @Getter
    @Builder
    public static class Response {
        @Schema(description = "결과 코드")
        private String code;
        @Schema(description = "결과 메시지")
        private String message;
        @Schema(description = "생성된 계좌 번호")
        private String accountNumber;
    }
}
