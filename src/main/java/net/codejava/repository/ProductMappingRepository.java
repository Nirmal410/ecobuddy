package net.codejava.repository;

import net.codejava.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProductMappingRepository extends JpaRepository<ProductMapping, Long> {
    
    List<ProductMapping> findByNormalProductContainingIgnoreCase(String keyword);
    
    List<ProductMapping> findByEcoAlternativeContainingIgnoreCase(String keyword);
    
    List<ProductMapping> findByCategory(String category);

    @Query("SELECT p FROM ProductMapping p WHERE LOWER(p.normalProduct) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(p.ecoAlternative) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :kw, '%'))")
    List<ProductMapping> searchByKeyword(@Param("kw") String kw);
}
