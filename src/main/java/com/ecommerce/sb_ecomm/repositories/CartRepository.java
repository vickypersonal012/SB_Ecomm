package com.ecommerce.sb_ecomm.repositories;

import com.ecommerce.sb_ecomm.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
