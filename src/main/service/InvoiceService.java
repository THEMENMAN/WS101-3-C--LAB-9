package com.Etac.John.Llyod.WS101_Act7.service;

import com.Etac.John.Llyod.WS101_Act7.model.Invoice;
import com.Etac.John.Llyod.WS101_Act7.model.Customer;
import com.Etac.John.Llyod.WS101_Act7.model.Product;
import com.Etac.John.Llyod.WS101_Act7.repository.InvoiceRepository;
import com.Etac.John.Llyod.WS101_Act7.repository.CustomerRepository;
import com.Etac.John.Llyod.WS101_Act7.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceService {
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    // Get all invoices
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
    
    // Get invoice by ID
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }
    
    // Get invoice by ID with products
    public Optional<Invoice> getInvoiceWithProducts(Long id) {
        return invoiceRepository.findByIdWithProducts(id);
    }
    
    // Create new invoice
    public Invoice createInvoice(Invoice invoice) {
        validateInvoice(invoice);
        
        // Ensure customer exists
        Customer customer = customerRepository.findById(invoice.getCustomer().getId())
            .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + invoice.getCustomer().getId()));
        
        invoice.setCustomer(customer);
        
        // Ensure all products exist and attach them
        if (invoice.getProducts() != null && !invoice.getProducts().isEmpty()) {
            List<Product> managedProducts = invoice.getProducts().stream()
                .map(product -> productRepository.findById(product.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + product.getId())))
                .collect(Collectors.toList());
            
            invoice.setProducts(managedProducts);
        }
        
        return invoiceRepository.save(invoice);
    }
    
    // Create invoice with customer and product IDs
    public Invoice createInvoice(Long customerId, List<Long> productIds) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
        
        Invoice invoice = new Invoice(customer);
        
        if (productIds != null && !productIds.isEmpty()) {
            List<Product> products = productIds.stream()
                .map(productId -> productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId)))
                .collect(Collectors.toList());
            
            invoice.setProducts(products);
        }
        
        return invoiceRepository.save(invoice);
    }
    
    // Update invoice
    public Optional<Invoice> updateInvoice(Long id, Invoice invoiceDetails) {
        return invoiceRepository.findById(id)
            .map(existingInvoice -> {
                if (invoiceDetails.getStatus() != null && !invoiceDetails.getStatus().trim().isEmpty()) {
                    existingInvoice.setStatus(invoiceDetails.getStatus());
                }
                if (invoiceDetails.getDueDate() != null) {
                    existingInvoice.setDueDate(invoiceDetails.getDueDate());
                }
                
                // Update products if provided
                if (invoiceDetails.getProducts() != null) {
                    List<Product> managedProducts = invoiceDetails.getProducts().stream()
                        .map(product -> productRepository.findById(product.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + product.getId())))
                        .collect(Collectors.toList());
                    
                    existingInvoice.setProducts(managedProducts);
                }
                
                return invoiceRepository.save(existingInvoice);
            });
    }
    
    // Delete invoice
    public boolean deleteInvoice(Long id) {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Get invoices by customer ID
    public List<Invoice> getInvoicesByCustomerId(Long customerId) {
        return invoiceRepository.findByCustomerId(customerId);
    }
    
    // Get invoices by status
    public List<Invoice> getInvoicesByStatus(String status) {
        return invoiceRepository.findByStatus(status);
    }
    
    // Get invoices by date range
    public List<Invoice> getInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return invoiceRepository.findByIssueDateBetween(startDate, endDate);
    }
    
    // Add product to invoice
    public Optional<Invoice> addProductToInvoice(Long invoiceId, Long productId) {
        return invoiceRepository.findById(invoiceId)
            .map(invoice -> {
                Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
                
                invoice.addProduct(product);
                return invoiceRepository.save(invoice);
            });
    }
    
    // Remove product from invoice
    public Optional<Invoice> removeProductFromInvoice(Long invoiceId, Long productId) {
        return invoiceRepository.findById(invoiceId)
            .map(invoice -> {
                Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
                
                invoice.removeProduct(product);
                return invoiceRepository.save(invoice);
            });
    }
    
    // Validate invoice data
    private void validateInvoice(Invoice invoice) {
        if (invoice.getCustomer() == null || invoice.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Invoice must have a customer");
        }
        if (invoice.getTotalAmount() != null && invoice.getTotalAmount() < 0) {
            throw new IllegalArgumentException("Invoice total amount cannot be negative");
        }
    }
}