package com.Etac.John.Llyod.WS101_Act7.repository;

import com.Etac.John.Llyod.WS101_Act7.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByCustomerId(Long customerId);
    
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    
    List<Invoice> findByStatus(String status);
    
    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.products WHERE i.id = :id")
    Optional<Invoice> findByIdWithProducts(@Param("id") Long id);
    
    List<Invoice> findByIssueDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Invoice> findByTotalAmountGreaterThanEqual(Double minAmount);
    
    List<Invoice> findByTotalAmountLessThanEqual(Double maxAmount);
}