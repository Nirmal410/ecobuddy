package net.codejava;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductMappingRepository extends JpaRepository<ProductMapping, Long> {
    List<ProductMapping> findByNormalProductContainingIgnoreCase(String keyword);
}
