package com.project.inventory_management.util;

import com.project.inventory_management.dto.MedicationRequestDTO;
import com.project.inventory_management.entity.Medication;
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
