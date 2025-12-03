package com.Etac.John.Llyod.WS101_Act7.repository;

import com.Etac.John.Llyod.WS101_Act7.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByFullNameContainingIgnoreCase(String fullName);
    
    Optional<Customer> findByEmail(String email);
    
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.invoices WHERE c.id = :id")
    Optional<Customer> findByIdWithInvoices(@Param("id") Long id);
    
    List<Customer> findByEmailContainingIgnoreCase(String email);
}