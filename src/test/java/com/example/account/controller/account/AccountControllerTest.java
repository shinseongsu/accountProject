package com.example.account.controller.account;

import com.example.account.constatnt.Code;
import com.example.account.dto.account.AccountResponseDto;
import com.example.account.dto.account.CreateAccountDto;
import com.example.account.dto.account.UnregisterAccountDto;
import com.example.account.service.account.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
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
        String accountNumber = "1234567890";
        Long memberId = 1L;

        UnregisterAccountDto request = UnregisterAccountDto.builder()
                .accountNumber(accountNumber)
                .memberId(memberId)
                .build();

        when(accountService.unregister(any()))
                        .thenReturn(UnregisterAccountDto.Response.builder()
                                .code(Code.UNREGISTER_SUCCESS.getCode())
                                .message(Code.UNREGISTER_SUCCESS.getMessage())
                                .memberId(memberId)
                                .accountNumber(accountNumber)
                                .unregisterDate(LocalDateTime.now())
                                .build());


        mockMvc.perform(put("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.UNREGISTER_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.UNREGISTER_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("계좌 생성 Test")
    void 계좌_생성_Test() throws Exception {
        Long memberId = 1L;
        Long balance = 1000L;
        String accountNumber = "1234567890";

        CreateAccountDto request = CreateAccountDto.builder()
                .memberId(memberId)
                .balance(balance)
                .build();

        when(accountService.createAccount(any()))
                .thenReturn(CreateAccountDto.Response.builder()
                        .code(Code.SUCESS.getCode())
                        .message(Code.SUCESS.getMessage())
                        .memberId(memberId)
                        .accountNumber(accountNumber)
                        .registerDate(LocalDateTime.now())
                        .build());

        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.SUCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.SUCESS.getMessage()))
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("계좌 조회 Test")
    void 계좌_조회_test() throws Exception {
        String testAccountNumber = "1234567890";
        Long balance = 1000L;
        Long memberId = 1L;

        when(accountService.getAccount(anyLong())).thenReturn(AccountResponseDto.builder()
                        .code(Code.SUCESS.getCode())
                        .message(Code.SUCESS.getMessage())
                        .accounts(List.of(AccountResponseDto.Accounts.builder()
                                .accountNumber(testAccountNumber)
                                .balance(balance)
                                .build()))
                        .build());

        mockMvc.perform(get("/account/" + memberId))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.SUCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.SUCESS.getMessage()))
                .andExpect(jsonPath("$.accounts[0].accountNumber").value(testAccountNumber))
                .andExpect(jsonPath("$.accounts[0].balance").value(balance))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("계좌번호 생성 Test")
    void 계좌번호_생성_TEST() throws Exception {
        String accountNumber = "0000000001";

        when(accountService.findMaxId())
                .thenReturn(accountNumber);

        assertEquals(accountService.findMaxId(), accountNumber);
    }

}
