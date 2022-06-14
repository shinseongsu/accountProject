package com.example.account.controller.transaction;

import com.example.account.constatnt.Code;
import com.example.account.domain.account.Account;
import com.example.account.dto.account.UnregisterAccountDto;
import com.example.account.dto.transcation.*;
import com.example.account.service.transaction.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    @Operation(summary = "계좌 입금 API", description = "계좌에 입금을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DepositDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERVAL SERVER ERROR")
    })
    public ResponseEntity<DepositDto.Response> deposit(@RequestBody @Valid DepositDto depositDto) {
        return ResponseEntity.ok(transactionService.deposit(depositDto));
    }

    @PostMapping("/withdraw")
    @Operation(summary = "계좌 출금 API", description = "계좌에 출금을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = WithdrawDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERVAL SERVER ERROR")
    })
    public ResponseEntity<WithdrawDto.Response> withdraw(@RequestBody @Valid WithdrawDto withdrawDto) {
        return ResponseEntity.ok(transactionService.withdraw(withdrawDto));
    }

    @PostMapping("/pay")
    @Operation(summary = "결제 API", description = "결제를 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PayDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERVAL SERVER ERROR")
    })
    public ResponseEntity<?> pay(@RequestBody @Valid PayDto request) {
        return ResponseEntity.ok(transactionService.pay(request));
    }

    @PostMapping("/cancel")
    @Operation(summary = "결제 취소 API", description = "결제 취소를 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CancelDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERVAL SERVER ERROR")
    })
    public ResponseEntity<?> cancel(@RequestBody @Valid CancelDto request) {
        return ResponseEntity.ok(transactionService.cancel(request));
    }

    @GetMapping("/transaction")
    @Operation(summary = "거래내역 조회 API", description = "거래내역을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = TransactionDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERVAL SERVER ERROR")
    })
    public ResponseEntity<?> transactionList(@RequestParam(value = "transaction_id") Long transactionId) {
        return ResponseEntity.ok(transactionService.transaction(transactionId));
    }

}
