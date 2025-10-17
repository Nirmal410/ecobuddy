package net.codejava;
import jakarta.persistence.*;

@Table(name = "product_mapping")
@Entity
public class ProductMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String normalProduct;
    private String description;
    private double price;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    private String imageUrl;

    // Getter and Setter for imageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNormalProduct() {
        return normalProduct;
    }

    public void setNormalProduct(String normalProduct) {
        this.normalProduct = normalProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
