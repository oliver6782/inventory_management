package com.project.inventorymanagement.util;

import com.project.inventorymanagement.dto.MedicationRequestDTO;
import com.project.inventorymanagement.entity.Medication;
import org.springframework.stereotype.Component;

@Component
public class MedicationConverter {
    public Medication convertToMedication(MedicationRequestDTO.AddMedicationDTO addMedicationDTO) {
        Medication medication = new Medication();
        medication.setName(addMedicationDTO.getName());
        medication.setDescription(addMedicationDTO.getDescription());
        medication.setQuantity(addMedicationDTO.getQuantity());
        medication.setType(addMedicationDTO.getType());
        return medication;
    }
}
