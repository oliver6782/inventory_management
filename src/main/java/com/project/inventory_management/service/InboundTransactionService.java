package com.project.inventory_management.service;

import com.project.inventory_management.dto.InboundTransactionDTO;
import com.project.inventory_management.entity.InboundTransaction;
import com.project.inventory_management.entity.Medication;
import com.project.inventory_management.mapper.InboundTransactionMapper;
import com.project.inventory_management.repository.InboundTransactionRepository;
import com.project.inventory_management.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class InboundTransactionService {

    private final InboundTransactionRepository inboundTransactionRepository;
    private final InboundTransactionMapper inboundTransactionMapper;
    private final MedicationRepository medicationRepository;

    @Autowired
    public InboundTransactionService(InboundTransactionRepository inboundTransactionRepository,
                                     InboundTransactionMapper inboundTransactionMapper,
                                     MedicationRepository medicationRepository) {
        this.inboundTransactionRepository = inboundTransactionRepository;
        this.inboundTransactionMapper = inboundTransactionMapper;
        this.medicationRepository = medicationRepository;
    }

    public InboundTransaction save(InboundTransactionDTO inboundTransactionDTO) {
        InboundTransaction inboundTransaction = inboundTransactionMapper
                .toInboundTransaction(inboundTransactionDTO);
        Medication medication = inboundTransaction.getMedication();
        medication.setQuantity(medication.getQuantity() + inboundTransactionDTO.getQuantity());
        medicationRepository.save(medication);
        inboundTransaction.setReceivedDate(Timestamp.from(Instant.now()));
        return inboundTransactionRepository.save(inboundTransaction);
    }

    public List<InboundTransactionDTO> findAll() {
        List<InboundTransaction> inboundTransactions = inboundTransactionRepository.findAll();
        return inboundTransactionMapper.toInboundTransactionDTOList(inboundTransactions);
    }

//    public InboundTransaction findById(Integer id) {
//        return inboundTransactionRepository.findById(id).orElseThrow(
//                () -> new NoSuchElementException("Cannot find inbound transaction with id: " + id)
//        );
//    }

}
