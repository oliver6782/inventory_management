package com.project.inventorymanagement.service;

import com.project.inventorymanagement.entity.Medication;
import com.project.inventorymanagement.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MedicationService {

    final private MedicationRepository medicationRepository;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> getAllMedications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page<Medication> medicationsPage = medicationRepository.findAll(pageable);
        return medicationsPage.getContent();
    }

    public Medication getMedicationById(long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Medication not found with id " + id));
    }

    public Medication saveMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication updateMedication(Medication medication, long id) {
        Medication medicationToUpdate = medicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Medication not found with id " + id));
        medicationToUpdate.setName(medication.getName());
        medicationToUpdate.setQuantity(medication.getQuantity());
        medicationToUpdate.setDescription(medication.getDescription());
        medicationToUpdate.setType(medication.getType());
        return medicationRepository.save(medicationToUpdate);
    }

    public Boolean deleteMedication(long id) {
        Medication medicationToDelete = medicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Medication not found with id " + id));
        medicationRepository.delete(medicationToDelete);
        return true;
    }
}

