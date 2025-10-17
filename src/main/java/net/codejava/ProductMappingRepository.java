package net.codejava;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductMappingRepository extends JpaRepository<ProductMapping, Long> {
    
    List<ProductMapping> findByNormalProductContainingIgnoreCase(String keyword);
    
    List<ProductMapping> findByEcoAlternativeContainingIgnoreCase(String keyword);
    
    List<ProductMapping> findByCategory(String category);
}
