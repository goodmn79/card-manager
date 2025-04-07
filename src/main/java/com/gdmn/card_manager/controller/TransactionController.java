package com.gdmn.card_manager.controller;

import com.gdmn.card_manager.dto.TransactionData;
import com.gdmn.card_manager.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Transaction controller")
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "Withdrawal operation")
    @PostMapping("/me/{card_id}/withdraw")
    @PreAuthorize("isAuthenticated()")
    public TransactionData withdraw(
            @PathVariable("card_id") long cardId,
            @RequestParam("amount") @DecimalMin(value = "0.0", message = "The amount must be at least 0") BigDecimal amount) {
        return transactionService.withdraw(cardId, amount);
    }

    @Operation(summary = "Transfer operation")
    @PostMapping("/me/{debit_card_id}/transfer/{transfer_card_id}")
    @PreAuthorize("isAuthenticated()")
    public List<TransactionData> transferBetweenOwnCards(
            @PathVariable("debit_card_id") long debitCardId,
            @PathVariable("transfer_card_id") long transferCardId,
            @RequestBody @DecimalMin(value = "0.0", message = "The amount must be at least 0") BigDecimal amount) {
        return transactionService.transferBetweenOwnCards(debitCardId, transferCardId, amount);
    }

    @Operation(summary = "Getting transaction by id")
    @GetMapping("/{id}")
    @PreAuthorize("@customSecurity.isDataOwnerOrAdmin(#id)")
    public TransactionData getTransactionById(@PathVariable long id) {
        return transactionService.getTransactionById(id);
    }

    @Operation(summary = "Getting authorized user transactions")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Page<TransactionData> getAllCurrentUserTransactions(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size) {
        return transactionService.getAllUserTransaction(page, size);
    }

    @Operation(summary = "Getting transactions by card id")
    @GetMapping("/card/{id}")
    @PreAuthorize("@customSecurity.isCardOwnerOrAdmin(#cardId)")
    public Page<TransactionData> getAllCardTransaction(@PathVariable("id") long cardId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size) {
        return transactionService.getAllCardTransaction(cardId, page, size);
    }

    @Operation(summary = "Getting authorized user transactions fir date")
    @GetMapping("/me/{date}")
    @PreAuthorize("isAuthenticated()")
    public Page<TransactionData> getAllUserTransactionForDate(@PathVariable LocalDate date,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size) {
        return transactionService.getAllUserTransactionForDate(date, page, size);
    }

    @Operation(summary = "Getting transactions for date by card id")
    @GetMapping("/card/{id}/{date}")
    @PreAuthorize("@customSecurity.isCardOwnerOrAdmin(#cardId)")
    public Page<TransactionData> getAllCardTransactionForDate(@PathVariable("id") long cardId,
                                                              @PathVariable LocalDate date,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size) {
        return transactionService.getAllCardTransactionForDate(cardId, date, page, size);
    }
}
