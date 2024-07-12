package com.project.inventorymanagement.service;

import com.project.inventorymanagement.entity.Medication;
import com.project.inventorymanagement.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class MedicationService {

    final private MedicationRepository medicationRepository;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    public Medication getMedicationById(int id) {
        return medicationRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Medication saveMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication updateMedication(Medication medication, int id) {
        Medication medicationToUpdate = medicationRepository.findById(id).get();
        if (Objects.isNull(medicationToUpdate)) {
            throw new NoSuchElementException("no such element");
        }
        return medicationRepository.save(medication);
    }

    public Boolean deleteMedication(int id) {
        Medication medication = medicationRepository.findById(id).get();
        medicationRepository.delete(medication);
        return true;
    }
}

