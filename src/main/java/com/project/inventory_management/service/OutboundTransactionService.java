package com.project.inventory_management.service;

import com.project.inventory_management.dto.OutboundTransactionDTO;
import com.project.inventory_management.entity.Medication;
import com.project.inventory_management.entity.OutboundTransaction;
import com.project.inventory_management.entity.User;
import com.project.inventory_management.exception.InsufficientStockException;
import com.project.inventory_management.mapper.OutboundTransactionMapper;
import com.project.inventory_management.repository.MedicationRepository;
import com.project.inventory_management.repository.OutboundTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class OutboundTransactionService {

    private final OutboundTransactionRepository outboundTransactionRepository;
    private final OutboundTransactionMapper outboundTransactionMapper;
    private final MedicationRepository MedicationRepository;
    private final InventoryCheckService inventoryCheckService;

    @Autowired
    public OutboundTransactionService(OutboundTransactionRepository outboundTransactionRepository,
                                      OutboundTransactionMapper outboundTransactionMapper,
                                      MedicationRepository medicationRepository,
                                      InventoryCheckService inventoryCheckService) {
        this.outboundTransactionRepository = outboundTransactionRepository;
        this.outboundTransactionMapper = outboundTransactionMapper;
        this.MedicationRepository = medicationRepository;
        this.inventoryCheckService = inventoryCheckService;
    }

    public OutboundTransaction save(OutboundTransactionDTO outboundTransactionDTO)
            throws InsufficientStockException {
        OutboundTransaction outboundTransaction = outboundTransactionMapper
                .toOutboundTransaction(outboundTransactionDTO);
        Medication medication = outboundTransaction.getMedication();
        int stock = medication.getQuantity();
        int requiredQuantity = outboundTransaction.getQuantity();
        if (stock < requiredQuantity) {
            throw new InsufficientStockException("Insufficient Stock");
        }
        medication.setQuantity(stock - requiredQuantity);
        outboundTransaction.setDispatchedDate(Timestamp.from(Instant.now()));
        MedicationRepository.save(medication);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        inventoryCheckService.checkInventory(email);
        return outboundTransactionRepository.save(outboundTransaction);
    }

    public List<OutboundTransactionDTO> findAll() {
        List<OutboundTransaction> outboundTransactions = outboundTransactionRepository.findAll();
        return outboundTransactionMapper.toOutboundTransactionDTOList(outboundTransactions);
    }

//    public OutboundTransaction findById(Integer id) {
//        return outboundTransactionRepository.findById(id).orElseThrow(
//                () -> new EntityNotFoundException("Outbound Transaction Not Found with id: " + id)
//        );
//    }
}
