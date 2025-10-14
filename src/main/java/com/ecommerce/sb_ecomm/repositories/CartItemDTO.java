package com.ecommerce.sb_ecomm.repositories;

import com.ecommerce.sb_ecomm.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemDTO extends JpaRepository<CartItem, Long> {
}
