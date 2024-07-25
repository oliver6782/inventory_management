package com.project.inventory_management.service;

import com.project.inventory_management.entity.Medication;
import com.project.inventory_management.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryCheckService {
    private final EmailService emailService;
    private final MedicationRepository MedicationRepository;

    @Autowired
    public InventoryCheckService(EmailService emailService,
                                 MedicationRepository medicationRepository) {
        this.emailService = emailService;
        this.MedicationRepository = medicationRepository;
    }

    @Async
    public void checkInventory(String email) {
        List<Medication> insufficientMedications = new ArrayList<>();
        for (Medication medication : MedicationRepository.findAll()) {
            if (medication.getQuantity() < 100) {
                insufficientMedications.add(medication);
            }
        }
        if (!insufficientMedications.isEmpty()) {
            emailService.sendEmail(email, insufficientMedications);
        }
    }


}
