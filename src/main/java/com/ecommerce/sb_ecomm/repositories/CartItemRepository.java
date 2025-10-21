package com.ecommerce.sb_ecomm.repositories;

import com.ecommerce.sb_ecomm.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
