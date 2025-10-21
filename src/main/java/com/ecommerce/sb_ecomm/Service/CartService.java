package com.ecommerce.sb_ecomm.Service;

import com.ecommerce.sb_ecomm.Payload.CartDTO;

public interface CartService {

    CartDTO addProductsToCart(Long productId, Integer quantity);
}
