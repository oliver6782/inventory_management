package com.project.inventory_management.mapper;

import com.project.inventory_management.dto.InboundTransactionDTO;
import com.project.inventory_management.entity.InboundTransaction;
import com.project.inventory_management.entity.Medication;
import com.project.inventory_management.exception.MedicationNotFoundException;
import com.project.inventory_management.repository.MedicationRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class InboundTransactionMapper {

    protected MedicationRepository medicationRepository;

    // using constructor injection in an abstract class
    // might not work directly with MapStruct.
    // Instead, you can use @Autowired on a setter method.
    @Autowired
    public void setMedicationRepository(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Mapping(source = "medicationId", target = "medication", qualifiedByName = "mapMedicationIdToMedication")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "supplier", target = "supplier")
    public abstract InboundTransaction toInboundTransaction(InboundTransactionDTO inboundTransactionDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "medication.id", target = "medicationId")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "supplier", target = "supplier")
    @Mapping(source = "receivedDate", target = "receivedDate")
    public abstract InboundTransactionDTO toInboundTransactionDTO(InboundTransaction inboundTransaction);

    public abstract List<InboundTransactionDTO> toInboundTransactionDTOList(List<InboundTransaction> inboundTransactionList);

    @Named("mapMedicationIdToMedication")
    protected Medication mapMedicationIdToMedication(Long medicationId) {
        return medicationRepository.findById(medicationId)
                .orElseThrow(() -> new MedicationNotFoundException("Medication not found"));
    }
}
