package net.codejava.entity;

import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "product_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductMapping {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "normal_product", nullable = false)
    private String normalProduct;
    
    @Column(name = "eco_alternative", nullable = false)
    private String ecoAlternative;
    
    @Column(length = 1000)
    private String description;
    
    @Column(name = "normal_price")
    private Double normalPrice;
    
    @Column(name = "eco_price")
    private Double ecoPrice;
    
    @Column(name = "purchase_link", length = 500)
    private String purchaseLink;
    
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @Column(name = "co2_saved_per_unit")
    private Double co2SavedPerUnit = 0.0;
    
    @Column(name = "plastic_saved_per_unit")
    private Double plasticSavedPerUnit = 0.0;
    
    @Column(length = 100)
    private String category;
    
    @Column(name = "ai_generated_pros", columnDefinition = "TEXT")
    private String aiGeneratedPros;
    
    @Column(name = "ai_generated_cons", columnDefinition = "TEXT")
    private String aiGeneratedCons;
    
    @Column(name = "ai_generated_suggestion", columnDefinition = "TEXT")
    private String aiGeneratedSuggestion;
}
