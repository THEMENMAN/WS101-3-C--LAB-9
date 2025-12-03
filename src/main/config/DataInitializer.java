package com.Etac.John.Llyod.WS101_Act7.config;

import com.Etac.John.Llyod.WS101_Act7.model.Customer;
import com.Etac.John.Llyod.WS101_Act7.model.Invoice;
import com.Etac.John.Llyod.WS101_Act7.model.Product;
import com.Etac.John.Llyod.WS101_Act7.repository.CustomerRepository;
import com.Etac.John.Llyod.WS101_Act7.repository.InvoiceRepository;
import com.Etac.John.Llyod.WS101_Act7.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Configuration
public class DataInitializer {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Bean
    @Transactional
    public CommandLineRunner initData() {
        return args -> {
            System.out.println("Initializing sample data...");
            
            // Clear existing data
            invoiceRepository.deleteAll();
            productRepository.deleteAll();
            customerRepository.deleteAll();
            
            // Create sample products
            Product laptop = new Product("Laptop Pro", 1299.99);
            Product mouse = new Product("Wireless Mouse", 29.99);
            Product keyboard = new Product("Mechanical Keyboard", 89.99);
            Product monitor = new Product("27-inch Monitor", 299.99);
            Product headphones = new Product("Noise Cancelling Headphones", 199.99);
            
            productRepository.saveAll(Arrays.asList(laptop, mouse, keyboard, monitor, headphones));
            
            // Create sample customers
            Customer john = new Customer("John Doe", "john.doe@email.com");
            john.setPhone("123-456-7890");
            
            Customer jane = new Customer("Jane Smith", "jane.smith@email.com");
            jane.setPhone("987-654-3210");
            
            Customer bob = new Customer("Bob Johnson", "bob.johnson@email.com");
            bob.setPhone("555-123-4567");
            
            customerRepository.saveAll(Arrays.asList(john, jane, bob));
            
            // Create sample invoices
            Invoice invoice1 = new Invoice(john);
            invoice1.addProduct(laptop);
            invoice1.addProduct(mouse);
            
            Invoice invoice2 = new Invoice(jane);
            invoice2.addProduct(keyboard);
            invoice2.addProduct(monitor);
            invoice2.addProduct(headphones);
            
            Invoice invoice3 = new Invoice(bob);
            invoice3.addProduct(laptop);
            invoice3.addProduct(headphones);
            
            invoiceRepository.saveAll(Arrays.asList(invoice1, invoice2, invoice3));
            
            System.out.println("Sample data initialized successfully!");
            System.out.println("Products created: " + productRepository.count());
            System.out.println("Customers created: " + customerRepository.count());
            System.out.println("Invoices created: " + invoiceRepository.count());
        };
    }
}