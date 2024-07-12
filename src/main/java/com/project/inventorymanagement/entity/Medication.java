package com.project.inventorymanagement.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medication {
    private enum Types {PRES, OTC, OTHER}

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private int quantity;

    @Column
    private Types type;

}

