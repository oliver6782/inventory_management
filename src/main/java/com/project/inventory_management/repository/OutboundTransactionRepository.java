package com.project.inventory_management.repository;

import com.project.inventory_management.entity.OutboundTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboundTransactionRepository extends JpaRepository<OutboundTransaction, Integer> {

}
