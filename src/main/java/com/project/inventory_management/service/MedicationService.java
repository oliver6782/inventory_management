package com.project.inventory_management.service;

import com.project.inventory_management.dto.FDADrugResponseDTO;
import com.project.inventory_management.dto.MedicationRequestDTO;
import com.project.inventory_management.entity.Medication;
import com.project.inventory_management.exception.FDAApiException;
import com.project.inventory_management.exception.MedicationExistException;
import com.project.inventory_management.exception.MedicationNotFoundException;
import com.project.inventory_management.repository.MedicationRepository;
import com.project.inventory_management.util.MedicationConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationConverter medicationConverter;
    private final RestTemplate restTemplate;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository,
                             MedicationConverter medicationConverter,
                             RestTemplate restTemplate) {
        this.medicationRepository = medicationRepository;
        this.medicationConverter = medicationConverter;
        this.restTemplate = restTemplate;
    }

    public List<Medication> getAllMedications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page<Medication> medicationsPage = medicationRepository.findAll(pageable);
        return medicationsPage.getContent();
    }

    public Medication getMedicationById(long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new MedicationNotFoundException(
                        "Medication not found with id " + id));
    }

    public Medication saveMedication(MedicationRequestDTO.AddMedicationDTO addMedicationDTO) {
        Optional<Medication> existMedication = medicationRepository.findByName(addMedicationDTO.getName());
        if (existMedication.isPresent()) {
            throw new MedicationExistException(
                    "Medication with name " + addMedicationDTO.getName() + " already exists");
        }
        return medicationRepository.save(medicationConverter.convertToMedication(addMedicationDTO));
    }

    public Medication updateMedication(
            MedicationRequestDTO.UpdateMedicationDTO updateMedicationDTO,
            long id) {
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

    public Boolean deleteMedication(long id) {
        Medication medicationToDelete = medicationRepository.findById(id)
                .orElseThrow(() -> new MedicationNotFoundException("Medication not found with id " + id));
        medicationRepository.delete(medicationToDelete);
        return true;
    }

    public FDADrugResponseDTO fetchMedicationInfo(String drugName) {
        String FDA_URL = "https://api.fda.gov/drug/label.json";
        String url = FDA_URL + "?search=active_ingredient:" + drugName + "&limit=1";
        try {
            return restTemplate
                    .getForEntity(url, FDADrugResponseDTO.class).getBody();
        } catch (FDAApiException ex) {
            throw new MedicationNotFoundException("FDA API error: " + ex.getMessage());
        }
    }
}
