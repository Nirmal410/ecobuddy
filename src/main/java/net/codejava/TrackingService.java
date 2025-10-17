package net.codejava;

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
        purchase.setCo2Saved(product.getCo2SavedPerUnit() * quantity);
        purchase.setPlasticSaved(product.getPlasticSavedPerUnit() * quantity);
        purchase.setPurchaseDate(LocalDateTime.now());
        
        purchaseHistoryRepository.save(purchase);
        
        // Update user's total stats
        user.setTotalCo2Saved(user.getTotalCo2Saved() + purchase.getCo2Saved());
        user.setTotalPlasticSaved(user.getTotalPlasticSaved() + purchase.getPlasticSaved());
        user.setTotalPurchases(user.getTotalPurchases() + 1);
        
        userRepository.save(user);
    }
    
    public Map<String, Object> getUserImpactStats(User user) {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalCo2Saved", user.getTotalCo2Saved());
        stats.put("totalPlasticSaved", user.getTotalPlasticSaved());
        stats.put("totalPurchases", user.getTotalPurchases());
        
        List<PurchaseHistory> recentPurchases = purchaseHistoryRepository
            .findByUserOrderByPurchaseDateDesc(user);
        
        stats.put("recentPurchases", recentPurchases);
        
        return stats;
    }
}
