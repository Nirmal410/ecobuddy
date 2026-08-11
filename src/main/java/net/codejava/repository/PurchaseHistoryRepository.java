package net.codejava.repository;

import net.codejava.entity.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
    
    @EntityGraph(attributePaths = {"product"})
    List<PurchaseHistory> findByUserOrderByPurchaseDateDesc(User user);
    
    @Query("SELECT SUM(p.co2Saved) FROM PurchaseHistory p WHERE p.user = ?1")
    Double getTotalCo2SavedByUser(User user);
    
    @Query("SELECT SUM(p.plasticSaved) FROM PurchaseHistory p WHERE p.user = ?1")
    Double getTotalPlasticSavedByUser(User user);
    
    @Query("SELECT COUNT(p) FROM PurchaseHistory p WHERE p.user = ?1")
    Long getTotalPurchasesByUser(User user);
}
