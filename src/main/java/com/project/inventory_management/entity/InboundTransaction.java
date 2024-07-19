package com.project.inventory_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
public class InboundTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(targetEntity = Medication.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "medication.id", nullable = false)
    private Medication medication;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date receivedDate;

    @Column(nullable = false)
    private String supplier;






}
