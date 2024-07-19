package com.project.inventory_management.controller;

import com.project.inventory_management.dto.OutboundTransactionDTO;
import com.project.inventory_management.entity.OutboundTransaction;
import com.project.inventory_management.exception.InsufficientStockException;
import com.project.inventory_management.mapper.OutboundTransactionMapper;
import com.project.inventory_management.service.OutboundTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/outbound/transactions")
public class OutboundTransactionController {

    private final OutboundTransactionService outboundTransactionService;
    private final OutboundTransactionMapper outboundTransactionMapper;

    @Autowired
    public OutboundTransactionController(OutboundTransactionService outboundTransactionService,
                                         OutboundTransactionMapper outboundTransactionMapper) {
        this.outboundTransactionService = outboundTransactionService;
        this.outboundTransactionMapper = outboundTransactionMapper;
    }

    @GetMapping
    public ResponseEntity<List<OutboundTransactionDTO>> getAllOutboundTransactions() {
        return ResponseEntity.of(Optional.ofNullable(outboundTransactionService.findAll()));
    }

    @PostMapping
    public ResponseEntity<OutboundTransaction> createOutboundTransaction(
            @Valid @RequestBody OutboundTransactionDTO outboundTransactionDTO)
            throws InsufficientStockException {
        return ResponseEntity.of(Optional.ofNullable(
                outboundTransactionService.save(outboundTransactionDTO)));
    }

}
