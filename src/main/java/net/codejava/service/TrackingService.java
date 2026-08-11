package net.codejava.service;

import net.codejava.entity.*;
import net.codejava.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrackingService {
    
    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public void recordPurchase(User user, ProductMapping product, Integer quantity) {
        PurchaseHistory purchase = new PurchaseHistory();
        purchase.setUser(user);
        purchase.setProduct(product);
        purchase.setQuantity(quantity);
        purchase.setCo2Saved((product.getCo2SavedPerUnit() != null ? product.getCo2SavedPerUnit() : 0.0) * quantity);
        purchase.setPlasticSaved((product.getPlasticSavedPerUnit() != null ? product.getPlasticSavedPerUnit() : 0.0) * quantity);
        purchase.setPurchaseDate(LocalDateTime.now());
        
        purchaseHistoryRepository.save(purchase);
        
        // Update user's total stats
        double currentCo2 = user.getTotalCo2Saved() != null ? user.getTotalCo2Saved() : 0.0;
        double currentPlastic = user.getTotalPlasticSaved() != null ? user.getTotalPlasticSaved() : 0.0;
        int currentPurchases = user.getTotalPurchases() != null ? user.getTotalPurchases() : 0;

        user.setTotalCo2Saved(currentCo2 + purchase.getCo2Saved());
        user.setTotalPlasticSaved(currentPlastic + purchase.getPlasticSaved());
        user.setTotalPurchases(currentPurchases + 1);
        
        userRepository.save(user);
    }
    
    public Map<String, Object> getUserImpactStats(User user) {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalCo2Saved", user.getTotalCo2Saved() != null ? user.getTotalCo2Saved() : 0.0);
        stats.put("totalPlasticSaved", user.getTotalPlasticSaved() != null ? user.getTotalPlasticSaved() : 0.0);
        stats.put("totalPurchases", user.getTotalPurchases() != null ? user.getTotalPurchases() : 0);
        
        List<PurchaseHistory> recentPurchases = purchaseHistoryRepository
            .findByUserOrderByPurchaseDateDesc(user);
        
        stats.put("recentPurchases", recentPurchases != null ? recentPurchases : List.of());
        
        return stats;
    }
}
