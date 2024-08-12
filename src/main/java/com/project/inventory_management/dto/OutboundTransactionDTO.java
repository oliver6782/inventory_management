package com.project.inventory_management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboundTransactionDTO {

    private Long id;

    @NotNull(message = "Medication ID can not be null")
    private Long medicationId;

    @NotNull(message = "Quantity can not be null")
    @Min(value = 1, message = "Amount must be greater than 1")
    private Integer quantity;

    @NotNull(message = "Supplier can not be null")
    private String receiver;

    private Date dispatchedDate;
}
