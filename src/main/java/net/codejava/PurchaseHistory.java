package net.codejava;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductMapping product;
    
    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;
    
    @Column(name = "co2_saved")
    private Double co2Saved;
    
    @Column(name = "plastic_saved")
    private Double plasticSaved;
    
    @Column(name = "quantity")
    private Integer quantity = 1;
    
    @PrePersist
    protected void onCreate() {
        purchaseDate = LocalDateTime.now();
    }
}
