package com.project.inventory_management.controller;

import com.project.inventory_management.dto.InboundTransactionDTO;
import com.project.inventory_management.entity.InboundTransaction;
import com.project.inventory_management.service.InboundTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inbound/transactions")
public class InboundTransactionController {

    private final InboundTransactionService inboundTransactionService;

    @Autowired
    public InboundTransactionController(InboundTransactionService inboundTransactionService) {
        this.inboundTransactionService = inboundTransactionService;
    }

    @GetMapping
    public ResponseEntity<List<InboundTransactionDTO>> getAllInboundTransactions() {
        return ResponseEntity.of(Optional.ofNullable(inboundTransactionService.findAll()));
    }

    @PostMapping
    public ResponseEntity<InboundTransaction> addInboundTransaction(
            @Valid @RequestBody InboundTransactionDTO inboundTransactionDTO) {
        return ResponseEntity.of(Optional.ofNullable(inboundTransactionService
                .save(inboundTransactionDTO)));
    }
}
