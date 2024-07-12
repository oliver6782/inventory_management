package com.project.inventorymanagement.controller;

import com.project.inventorymanagement.entity.Medication;
import com.project.inventorymanagement.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    private final MedicationService medicationService;

    @Autowired
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public List<Medication> getAllMedications() {
        return medicationService.getAllMedications();
        // limit jpa
    }

    @GetMapping("/{id}")
    public Medication getMedicationById(@PathVariable int id) {
        return medicationService.getMedicationById(id);
    }

    @PostMapping
    public ResponseEntity<Medication> addMedication(@RequestBody Medication medication) {
        return ResponseEntity.ok(medicationService.saveMedication(medication));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medication> updateMedication(@RequestBody Medication medication, @PathVariable int id) {
        return ResponseEntity.ok(medicationService.updateMedication(medication, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMedication(@PathVariable int id) {
        return ResponseEntity.ok(medicationService.deleteMedication(id));
    }
}

