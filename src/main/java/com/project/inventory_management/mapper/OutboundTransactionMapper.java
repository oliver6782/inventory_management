package com.project.inventory_management.mapper;

import com.project.inventory_management.dto.OutboundTransactionDTO;
import com.project.inventory_management.entity.Medication;
import com.project.inventory_management.entity.OutboundTransaction;
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
public abstract class OutboundTransactionMapper {

    @Autowired
    MedicationRepository medicationRepository;

    @Mapping(source = "medicationId", target = "medication", qualifiedByName = "mapMedicationIdToMedication")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "receiver", target = "receiver")
    public abstract OutboundTransaction toOutboundTransaction(OutboundTransactionDTO outboundTransactionDTO);

    @Mapping(source = "medication.id", target = "medicationId")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "receiver", target = "receiver")
    @Mapping(source = "dispatchedDate", target = "dispatchedDate")
    public abstract OutboundTransactionDTO toOutboundTransactionDTO(OutboundTransaction outboundTransaction);

    public abstract List<OutboundTransactionDTO> toOutboundTransactionDTOList(List<OutboundTransaction> list);

    @Named("mapMedicationIdToMedication")
    protected Medication mapMedicationIdToMedication(Long medicationId) {
        return medicationRepository.findById(medicationId)
                .orElseThrow(() -> new MedicationNotFoundException("Medication not found"));
    }
}
