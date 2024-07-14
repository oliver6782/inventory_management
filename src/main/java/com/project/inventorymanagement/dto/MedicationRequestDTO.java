package com.project.inventorymanagement.dto;

import com.project.inventorymanagement.entity.Medication;
import com.project.inventorymanagement.validation.ValidEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MedicationRequestDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddMedicationDTO {

        @NotBlank(message = "Name is required")
        @Size(min = 4, max = 50, message = "Medication name must be at least 4 characters")
        private String name;

        @NotBlank(message = "Description is required")
        @Size(max = 5000, message = "Description should be less than 5000 characters")
        private String description;

        @NotNull(message = "Quantity can not be null")
        @Min(value = 1, message = "Amount must be greater than 1")
        private Integer quantity;

        @NotNull(message = "Type can not be null")
        @ValidEnum(enumClass = Medication.Types.class)
        private Medication.Types type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateMedicationDTO {

        @Size(min = 4, max = 50, message = "Medication name must be at least 4 characters")
        private String name;

        @Size(max = 5000, message = "Description should be less than 5000 characters")
        private String description;

        @Min(value = 1, message = "Amount must be greater than 1")
        private Integer quantity;

        @NotNull(message = "Type can not be null")
        @ValidEnum(enumClass = Medication.Types.class)
        private Medication.Types type;
    }

}
