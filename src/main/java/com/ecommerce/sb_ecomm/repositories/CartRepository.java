package com.ecommerce.sb_ecomm.repositories;

import com.ecommerce.sb_ecomm.model.Cart;
import com.ecommerce.sb_ecomm.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c from Cart c where c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("select ci from CartItem ci where ci.cart.cartId = ?1 and  ci.product.productId = ?2")
    CartItem findCartItemByProductIdAndCartId(Long cartId, Long productId);
}
