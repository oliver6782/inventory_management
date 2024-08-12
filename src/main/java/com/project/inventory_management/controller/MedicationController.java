package com.project.inventory_management.controller;

import com.project.inventory_management.dto.FDADrugResponseDTO;
import com.project.inventory_management.dto.MedicationRequestDTO;
import com.project.inventory_management.entity.Medication;
import com.project.inventory_management.service.MedicationService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<Medication>> getAllMedications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(medicationService.getAllMedications(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable long id) {
        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }

    @PostMapping
    public ResponseEntity<Medication> addMedication(
            @Valid @RequestBody MedicationRequestDTO.AddMedicationDTO addMedicationDTO) {
        return ResponseEntity.ok(medicationService.saveMedication(addMedicationDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medication> updateMedication(
            @Valid @RequestBody MedicationRequestDTO.UpdateMedicationDTO updateMedicationDTO,
            @PathVariable long id) {
        return ResponseEntity.ok(medicationService.updateMedication(updateMedicationDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMedication(@PathVariable long id) {
        return ResponseEntity.ok(medicationService.deleteMedication(id));
    }

//    @GetMapping("/medication-not-found")
//    public MedicationNotFoundException medicationNotFound() {
//        return new MedicationNotFoundException("Medication not found");
//    }

    @GetMapping("/fetch-data/{drugName}")
    public ResponseEntity<FDADrugResponseDTO> fetchMedicationInfo(@PathVariable String drugName) {
        return ResponseEntity.ok(medicationService.fetchMedicationInfo(drugName));
    }
}
