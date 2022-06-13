package com.example.account.controller.account;

import com.example.account.constatnt.Code;
import com.example.account.domain.account.Account;
import com.example.account.domain.account.AccountStatus;
import com.example.account.domain.member.Member;
import com.example.account.dto.account.AccountResponseDto;
import com.example.account.dto.account.CreateAccountDto;
import com.example.account.dto.account.UnregisterAccountDto;
import com.example.account.repository.account.AccountRepository;
import com.example.account.repository.member.MemberRepository;
import com.example.account.service.account.AccountService;
import com.example.account.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AccountControllerTest {

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("계좌 정지 Test")
    void 계좌_정지() throws Exception {
        UnregisterAccountDto.Request request = UnregisterAccountDto.Request.builder()
                .accountNumber("123213")
                .build();

        doNothing().when(accountService).unregister(any());

        mockMvc.perform(delete("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.UNREGISTER_SUCCESS.getCode()))
                .andExpect(jsonPath("$.meessage").value(Code.UNREGISTER_SUCCESS.getMessage()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("계좌 생성 Test")
    void 계좌_생성_Test() throws Exception {
        CreateAccountDto.Request request = CreateAccountDto.Request.builder()
                .name("신성수")
                .phoneNumber("01020834409")
                .birthDay("19950918")
                .password("1111")
                .build();
        String testAccountNumber = "1492159028";

        when(accountService.createAccount(any())).thenReturn(testAccountNumber);

        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.SUCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.SUCESS.getMessage()))
                .andExpect(jsonPath("$.accountNumber").value(testAccountNumber))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("계좌 조회 Test")
    void 계좌_조회_test() throws Exception {
        String testAccountNumber = "1492159028";
        Long balance = 1000L;
        AccountStatus accountStatus = AccountStatus.IN_USE;

        when(accountService.getAccount(anyString())).thenReturn(AccountResponseDto.builder()
                        .code(Code.SUCESS.getCode())
                        .message(Code.SUCESS.getMessage())
                        .accountNumber(testAccountNumber)
                        .balance(balance)
                        .accountStatus(accountStatus)
                        .build());

        mockMvc.perform(get("/account/" + testAccountNumber))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.SUCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.SUCESS.getMessage()))
                .andExpect(jsonPath("$.accountNumber").value(testAccountNumber))
                .andExpect(jsonPath("$.balance").value(balance))
                .andExpect(jsonPath("$.accountStatus").value("IN_USE"))
                .andExpect(status().isOk());
    }


}
