package com.Etac.John.Llyod.WS101_Act7.service;

import com.Etac.John.Llyod.WS101_Act7.model.Customer;
import com.Etac.John.Llyod.WS101_Act7.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    // Get customer by ID
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }
    
    // Get customer by ID with invoices
    public Optional<Customer> getCustomerWithInvoices(Long id) {
        return customerRepository.findByIdWithInvoices(id);
    }
    
    // Create new customer
    public Customer createCustomer(Customer customer) {
        validateCustomer(customer);
        
        // Check if email already exists
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Customer with email '" + customer.getEmail() + "' already exists");
        }
        
        return customerRepository.save(customer);
    }
    
    // Update customer
    public Optional<Customer> updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
            .map(existingCustomer -> {
                if (customerDetails.getFullName() != null && !customerDetails.getFullName().trim().isEmpty()) {
                    existingCustomer.setFullName(customerDetails.getFullName());
                }
                if (customerDetails.getEmail() != null && !customerDetails.getEmail().trim().isEmpty()) {
                    // Check if new email is already taken by another customer
                    if (!existingCustomer.getEmail().equals(customerDetails.getEmail())) {
                        if (customerRepository.findByEmail(customerDetails.getEmail()).isPresent()) {
                            throw new IllegalArgumentException("Email '" + customerDetails.getEmail() + "' is already in use");
                        }
                        existingCustomer.setEmail(customerDetails.getEmail());
                    }
                }
                if (customerDetails.getPhone() != null) {
                    existingCustomer.setPhone(customerDetails.getPhone());
                }
                return customerRepository.save(existingCustomer);
            });
    }
    
    // Delete customer
    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Search customers by name
    public List<Customer> searchCustomersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllCustomers();
        }
        return customerRepository.findByFullNameContainingIgnoreCase(name);
    }
    
    // Search customers by email
    public List<Customer> searchCustomersByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return getAllCustomers();
        }
        return customerRepository.findByEmailContainingIgnoreCase(email);
    }
    
    // Get customer by email
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
    
    // Validate customer data
    private void validateCustomer(Customer customer) {
        if (customer.getFullName() == null || customer.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer full name cannot be empty");
        }
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be empty");
        }
        if (!customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}