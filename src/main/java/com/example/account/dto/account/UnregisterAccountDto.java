package com.example.account.dto.account;

import com.example.account.constatnt.Code;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;

public class UnregisterAccountDto {

    @Schema(description = "계좌 해지 요청 DTO")
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @Schema(description = "계좌 번호")
        @NotEmpty
        private String accountNumber;
    }

    @Schema(description = "계좌 해지 응답 DTO")
    @Getter @Setter
    @Builder
    public static class Response {
        @Schema(description = "결과 코드")
        private String code;
        @Schema(description = "결과 메시지")
        private String meessage;

        public static Response success() {
            return Response.builder()
                    .code(Code.UNREGISTER_SUCCESS.getCode())
                    .meessage(Code.UNREGISTER_SUCCESS.getMessage())
                    .build();
        }
    }

}
