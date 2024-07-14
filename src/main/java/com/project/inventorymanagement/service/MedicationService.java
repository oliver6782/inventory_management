package com.project.inventorymanagement.service;

import com.project.inventorymanagement.dto.MedicationRequestDTO;
import com.project.inventorymanagement.entity.Medication;
import com.project.inventorymanagement.exception.MedicationNotFoundException;
import com.project.inventorymanagement.repository.MedicationRepository;
import com.project.inventorymanagement.util.MedicationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationConverter medicationConverter;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository,
                             MedicationConverter medicationConverter) {
        this.medicationRepository = medicationRepository;
        this.medicationConverter = medicationConverter;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Medication> getAllMedications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page<Medication> medicationsPage = medicationRepository.findAll(pageable);
        return medicationsPage.getContent();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Medication getMedicationById(long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new MedicationNotFoundException("Medication not found with id " + id));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Medication saveMedication(MedicationRequestDTO.AddMedicationDTO addMedicationDTO) {
        return medicationRepository.save(medicationConverter.convertToMedication(addMedicationDTO));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Medication updateMedication(MedicationRequestDTO.UpdateMedicationDTO updateMedicationDTO, long id) {
        Medication medicationToUpdate = medicationRepository.findById(id)
                .orElseThrow(() -> new MedicationNotFoundException("Medication not found with id " + id));
        if (updateMedicationDTO.getName() != null) {
            medicationToUpdate.setName(updateMedicationDTO.getName());
        }
        if (updateMedicationDTO.getDescription() != null) {
            medicationToUpdate.setDescription(updateMedicationDTO.getDescription());
        }
        if (updateMedicationDTO.getQuantity() != null) {
            medicationToUpdate.setQuantity(updateMedicationDTO.getQuantity());
        }
        if (updateMedicationDTO.getType() != null) {
            medicationToUpdate.setType(updateMedicationDTO.getType());
        }
        return medicationRepository.save(medicationToUpdate);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Boolean deleteMedication(long id) {
        Medication medicationToDelete = medicationRepository.findById(id)
                .orElseThrow(() -> new MedicationNotFoundException("Medication not found with id " + id));
        medicationRepository.delete(medicationToDelete);
        return true;
    }
}
