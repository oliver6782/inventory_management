package com.project.inventorymanagement.controller;

import com.project.inventorymanagement.entity.Medication;
import com.project.inventorymanagement.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER', 'ADMIN')")
    public ResponseEntity<List<Medication>> getAllMedications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(medicationService.getAllMedications(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER', 'ADMIN')")
    public ResponseEntity<Medication> getMedicationById(@PathVariable long id) {
        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER', 'ADMIN')")
    public ResponseEntity<Medication> addMedication(@RequestBody Medication medication) {
        return ResponseEntity.ok(medicationService.saveMedication(medication));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER', 'ADMIN')")
    public ResponseEntity<Medication> updateMedication(
            @RequestBody Medication medication, @PathVariable int id) {
        return ResponseEntity.ok(medicationService.updateMedication(medication, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER', 'ADMIN')")
    public ResponseEntity<Boolean> deleteMedication(@PathVariable long id) {
        return ResponseEntity.ok(medicationService.deleteMedication(id));
    }
}

