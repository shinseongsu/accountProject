package com.example.account.controller.trnsaction;

import com.example.account.constatnt.Code;
import com.example.account.domain.account.Account;
import com.example.account.domain.account.AccountStatus;
import com.example.account.domain.transaction.TransactionStatus;
import com.example.account.dto.transcation.*;
import com.example.account.service.transaction.TransactionService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("계좌 입금 Test")
    void 계좌_입금_test() throws Exception {
        String accountNumber = "1234567890";
        Long amount = 1000L;

        DepositDto depositDto = DepositDto.builder()
                .memberId(1L)
                .accountNumber(accountNumber)
                .amount(amount)
                .build();

        when(transactionService.deposit(any())).thenReturn(DepositDto.Response.builder()
                        .code(Code.DEPOSIT_SUCCESS.getCode())
                        .message(Code.DEPOSIT_SUCCESS.getMessage())
                        .accountNumber(accountNumber)
                        .transactionResult(0L)
                        .transactionId(1L)
                        .amount(amount)
                        .transactionDate(LocalDateTime.now())
                        .build());

        mockMvc.perform(post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDto)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.DEPOSIT_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.DEPOSIT_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.transactionResult").value(0L))
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("계좌 출금 Test")
    void 계좌_출금_test() throws Exception {
        String accountNumber = "1234567890";
        Long amount = 1000L;

        WithdrawDto request = WithdrawDto.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .memberId(1L)
                .build();

        when(transactionService.withdraw(any())).thenReturn(WithdrawDto.Response.builder()
                .code(Code.WITHDRAW_SUCCESS.getCode())
                .message(Code.WITHDRAW_SUCCESS.getMessage())
                .accountNumber(accountNumber)
                .transactionResult(0L)
                .transactionId(1L)
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .build());


        mockMvc.perform(post("/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.WITHDRAW_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.WITHDRAW_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.transactionResult").value(0L))
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("결제 Test")
    void 결제_test() throws Exception {
        String accountNumber = "1234567890";
        Long amount = 1000L;

        PayDto request = PayDto.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .memberId(1L)
                .build();

        when(transactionService.pay(any())).thenReturn(PayDto.Response.builder()
                .code(Code.PAY_SUCCESS.getCode())
                .message(Code.PAY_SUCCESS.getMessage())
                        .accountNumber(accountNumber)
                        .transactionResult(amount)
                        .transactionId(1L)
                        .amount(amount)
                        .transactionDate(LocalDateTime.now())
                .build());

        mockMvc.perform(post("/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.PAY_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.PAY_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.accountNumber").value(Long.valueOf(accountNumber)))
                .andExpect(jsonPath("$.transactionResult").value(amount))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("결제 취소 Test")
    void 결제취소_test() throws Exception {
        String accountNumber = "1248124912";
        Long amount = 1000L;

        CancelDto request = CancelDto.builder()
                .transactionId(1L)
                .accountNumber(accountNumber)
                .amount(amount)
                .build();

        when(transactionService.cancel(any())).thenReturn(CancelDto.Response.builder()
                .code(Code.CANCEL_SUCCESS.getCode())
                .message(Code.CANCEL_SUCCESS.getMessage())
                .accountNumber(accountNumber)
                .transactionResult(amount)
                .transactionId(1L)
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .build());

        mockMvc.perform(post("/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.CANCEL_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.CANCEL_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.transactionResult").value(amount))
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("거래조회 Test")
    void 결제_조회_test() throws Exception {
        String accountNumber = "1234567890";
        Long balance = 1000L;
        Long transcationId = 1L;
        TransactionStatus transactionStatus = TransactionStatus.PAY;

        when(transactionService.transaction(anyLong())).thenReturn(TransactionDto.builder()
                        .code(Code.SUCESS.getCode())
                        .message(Code.SUCESS.getMessage())
                        .accountNumber(accountNumber)
                        .transactionStatus(transactionStatus)
                        .transactionResult(balance)
                        .transactionId(1L)
                        .amount(balance)
                        .transactionDate(LocalDateTime.now())
                        .build());


        mockMvc.perform(get("/transaction?transaction_id=1"))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.SUCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.SUCESS.getMessage()))
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.transactionStatus").value(transactionStatus.toString()))
                .andExpect(jsonPath("$.transactionResult").value(balance))
                .andExpect(jsonPath("$.transactionId").value(transcationId))
                .andExpect(jsonPath("$.amount").value(balance))
                .andExpect(status().isOk());

    }

}
