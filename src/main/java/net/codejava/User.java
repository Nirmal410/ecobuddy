package net.codejava;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 45)
    private String email;
    
    @Column(nullable = false, length = 64)
    private String password;
    
    @Column(nullable = false, length = 45)
    private String firstName;
    
    @Column(nullable = false, length = 20)
    private String lastName;
    
    @Column(name = "total_co2_saved")
    private Double totalCo2Saved = 0.0;
    
    @Column(name = "total_plastic_saved")
    private Double totalPlasticSaved = 0.0;
    
    @Column(name = "total_purchases")
    private Integer totalPurchases = 0;
}
