package com.smartticket.event.internal.persistence;

import com.smartticket.event.internal.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByActiveTrue();

    boolean existsByNameIgnoreCase(String name);
}
