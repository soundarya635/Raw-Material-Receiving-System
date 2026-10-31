package com.receivingyard.repository;

import com.receivingyard.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByCode(String code);
    Optional<Material> findByName(String name);
    boolean existsByCode(String code);
}
