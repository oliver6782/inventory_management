package com.project.inventorymanagement.controller;

import com.project.inventorymanagement.dto.MedicationRequestDTO;
import com.project.inventorymanagement.entity.Medication;
import com.project.inventorymanagement.service.MedicationService;
import com.project.inventorymanagement.util.ValidationUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    private final MedicationService medicationService;
    private final ValidationUtil validationUtil;

    @Autowired
    public MedicationController(MedicationService medicationService,
                                ValidationUtil validationUtil) {
        this.medicationService = medicationService;
        this.validationUtil = validationUtil;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Medication>> getAllMedications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(medicationService.getAllMedications(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Medication> getMedicationById(@PathVariable long id) {
        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> addMedication(
            @Valid @RequestBody MedicationRequestDTO.AddMedicationDTO addMedicationDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationUtil.generateValidationErrorResponse(bindingResult);
        }
        return ResponseEntity.ok(medicationService.saveMedication(addMedicationDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> updateMedication(
            @Valid @RequestBody MedicationRequestDTO.UpdateMedicationDTO updateMedicationDTO,
            BindingResult bindingResult,
            @PathVariable long id) {
        if (bindingResult.hasErrors()) {
            return validationUtil.generateValidationErrorResponse(bindingResult);
        }
        return ResponseEntity.ok(medicationService.updateMedication(updateMedicationDTO, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Boolean> deleteMedication(@PathVariable long id) {
        return ResponseEntity.ok(medicationService.deleteMedication(id));
    }
}
