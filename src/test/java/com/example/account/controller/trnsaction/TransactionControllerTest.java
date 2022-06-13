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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
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
        String accountNumber = "1248124912";
        String amount = "1000";

        DepositDto.Request request = DepositDto.Request.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .build();

        when(transactionService.deposit(any())).thenReturn(Account.builder()
                        .accountNumber(accountNumber)
                        .password("111")
                        .balance(Long.valueOf(amount))
                        .accountStatus(AccountStatus.IN_USE)
                        .build());

        mockMvc.perform(post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.DEPOSIT_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.DEPOSIT_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.currentBalance").value(amount))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("계좌 출금 Test")
    void 계좌_출금_test() throws Exception {
        String accountNumber = "1248124912";
        String amount = "1000";
        String password = "1111";

        WithdrawDto.Request request = WithdrawDto.Request.builder()
                .accountNumber(accountNumber)
                .amount(amount)
                .password(password)
                .build();

        when(transactionService.withdraw(any())).thenReturn(Account.builder()
                .accountNumber(accountNumber)
                .password("111")
                .balance(Long.valueOf(amount))
                .accountStatus(AccountStatus.IN_USE)
                .build());


        mockMvc.perform(post("/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.WITHDRAW_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.WITHDRAW_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.currentBalance").value(amount))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("결제 Test")
    void 결제_test() throws Exception {
        String accountNumber = "1248124912";
        String orderName = "테스트 상품";
        String amount = "1000";
        String password = "1111";
        Long orderId = 1000L;

        PayDto.Request request = PayDto.Request.builder()
                .accountNumber(accountNumber)
                .orderName(orderName)
                .amount(amount)
                .password(password)
                .build();

        when(transactionService.pay(any())).thenReturn(PayDto.Response.builder()
                .code(Code.PAY_SUCCESS.getCode())
                .message(Code.PAY_SUCCESS.getMessage())
                .orderId(orderId)
                .orderName(request.getOrderName())
                .balance(Long.valueOf(request.getAmount()))
                .accountBalance(Long.valueOf(amount))
                .build());

        mockMvc.perform(post("/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.PAY_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.PAY_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.orderName").value(orderName))
                .andExpect(jsonPath("$.balance").value(amount))
                .andExpect(jsonPath("$.accountBalance").value(Long.valueOf(amount)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("결제 취소 Test")
    void 결제취소_test() throws Exception {
        String accountNumber = "1248124912";
        String amount = "1000";
        String password = "1111";
        String orderId = "1";
        String orderName = "테스트 상품";

        CancelDto.Request request = CancelDto.Request.builder()
                .orderId(orderId)
                .accountNumber(accountNumber)
                .password(password)
                .build();

        when(transactionService.cancel(any())).thenReturn(CancelDto.Response.builder()
                .code(Code.CANCEL_SUCCESS.getCode())
                .message(Code.CANCEL_SUCCESS.getMessage())
                .orderId(Long.valueOf(orderId))
                .orderName(orderName)
                .balance(Long.valueOf(amount))
                .accountBalance(Long.valueOf(amount))
                .build());

        mockMvc.perform(post("/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.CANCEL_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.CANCEL_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.orderName").value(orderName))
                .andExpect(jsonPath("$.balance").value(amount))
                .andExpect(jsonPath("$.accountBalance").value(Long.valueOf(amount)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("거래조회 Test")
    void 결제_조회_test() throws Exception {
        String accountNumber = "124124213";
        Long orderId = 1L;
        String orderName = "테스트 상품";
        Integer totalPage = 10;
        Long balance = 1000L;
        TransactionStatus transactionStatus = TransactionStatus.PAY;
        LocalDateTime current = LocalDateTime.now();

        when(transactionService.transaction(anyString(), any())).thenReturn(TransactionDto.builder()
                        .code(Code.SUCESS.getCode())
                        .message(Code.SUCESS.getMessage())
                        .accountNumber(accountNumber)
                        .totalPage(totalPage)
                        .transactionList(List.of( TransactionList.builder()
                                .orderId(orderId)
                                .orderName(orderName)
                                .balance(balance)
                                .status(transactionStatus)
                                .transactionDate(current)
                                .build()))
                        .build());


        mockMvc.perform(get("/transaction?accountNumber=" + accountNumber))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.SUCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.SUCESS.getMessage()))
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.totalPage").value(totalPage))
                .andExpect(jsonPath("$.transactionList[0].orderId").value(orderId))
                .andExpect(jsonPath("$.transactionList[0].orderName").value(orderName))
                .andExpect(jsonPath("$.transactionList[0].balance").value(balance))
                .andExpect(jsonPath("$.transactionList[0].status").value(transactionStatus.toString()))
                .andExpect(status().isOk());

    }

}
