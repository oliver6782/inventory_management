package com.project.inventory_management.repository;

import com.project.inventory_management.entity.InboundTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboundTransactionRepository extends JpaRepository<InboundTransaction, Integer> {

}
