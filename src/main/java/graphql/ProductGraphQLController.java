package graphql;

// Removed unresolved import; using the package-private Product class defined below.
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

// Minimal local ProductService interface to satisfy the controller compile-time dependency.
// Implementations can remain in their original package; this local interface mirrors the methods used here.
interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    List<Product> searchProductsByName(String name);
    Product createProduct(Product product);
    Optional<Product> updateProduct(Long id, Product product);
    Boolean deleteProduct(Long id);
}

@Controller
public class ProductGraphQLController {

    private final ProductService productService;

    public ProductGraphQLController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @QueryMapping
    public Optional<Product> getProductById(@Argument Long id) {
        return productService.getProductById(id);
    }

    @QueryMapping
    public List<Product> searchProducts(@Argument String name) {
        return productService.searchProductsByName(name);
    }

    @QueryMapping
    public Integer getProductsCount() {
        return productService.getAllProducts().size();
    }

    @MutationMapping
    public Product createProduct(@Argument String name, @Argument Double price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return productService.createProduct(product);
    }

    @MutationMapping
    public Optional<Product> updateProduct(@Argument Long id, @Argument String name, @Argument Double price) {
        Product productDetails = new Product();
        productDetails.setName(name);
        productDetails.setPrice(price);
        return productService.updateProduct(id, productDetails);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        return productService.deleteProduct(id);
    }

    @MutationMapping
    public Product addProduct(@Argument ProductInput productInput) {
        Product product = new Product();
        product.setName(productInput.name());
        product.setPrice(productInput.price());
        return productService.createProduct(product);
    }

    public record ProductInput(String name, Double price) {}
}

// Minimal package-private Product class used by this controller when the original model package isn't available.
class Product {
    private Long id;
    private String name;
    private Double price;

    public Product() {}

    public Product(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}