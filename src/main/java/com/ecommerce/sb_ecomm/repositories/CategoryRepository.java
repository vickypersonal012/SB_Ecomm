package com.ecommerce.sb_ecomm.repositories;

import com.ecommerce.sb_ecomm.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
