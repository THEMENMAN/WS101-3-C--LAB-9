package com.Etac.John.Llyod.WS101_Act7.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;
    
    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;
    
    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, PAID, CANCELLED
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "invoice_products",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();
    
    // Constructors
    public Invoice() {
        this.issueDate = LocalDateTime.now();
        this.dueDate = this.issueDate.plusDays(30);
        this.invoiceNumber = generateInvoiceNumber();
    }
    
    public Invoice(Customer customer) {
        this();
        this.customer = customer;
        this.totalAmount = 0.0;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    
    public LocalDateTime getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDateTime issueDate) { this.issueDate = issueDate; }
    
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { 
        this.products = products;
        calculateTotal();
    }
    
    // Helper methods
    private String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis();
    }
    
    public void addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
            product.getInvoices().add(this);
            calculateTotal();
        }
    }
    
    public void removeProduct(Product product) {
        products.remove(product);
        product.getInvoices().remove(this);
        calculateTotal();
    }
    
    private void calculateTotal() {
        this.totalAmount = products.stream()
            .mapToDouble(Product::getPrice)
            .sum();
    }
    
    @Override
    public String toString() {
        return "Invoice{id=" + id + ", invoiceNumber='" + invoiceNumber + 
               "', totalAmount=" + totalAmount + ", status='" + status + "'}";
    }
}