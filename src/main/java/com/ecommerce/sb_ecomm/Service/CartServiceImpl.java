package com.ecommerce.sb_ecomm.Service;

import com.ecommerce.sb_ecomm.Payload.CartDTO;
import com.ecommerce.sb_ecomm.Payload.ProductDTO;
import com.ecommerce.sb_ecomm.exceptions.APIException;
import com.ecommerce.sb_ecomm.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecomm.model.Cart;
import com.ecommerce.sb_ecomm.model.CartItem;
import com.ecommerce.sb_ecomm.model.Product;
import com.ecommerce.sb_ecomm.repositories.CartItemRepository;
import com.ecommerce.sb_ecomm.repositories.CartRepository;
import com.ecommerce.sb_ecomm.repositories.ProductRepository;
import com.ecommerce.sb_ecomm.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(AuthUtil authUtil,
                           ModelMapper modelMapper,
                           ProductRepository productRepository,
                           CartRepository cartRepository,
                           CartItemRepository cartItemRepository) {
        this.authUtil = authUtil;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }


    @Override
    public CartDTO addProductsToCart(Long productId, Integer quantity) {
        Cart cart = createCart();

        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("Product", "Product ID", productId));

        CartItem cartItem = cartRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists");
        }

        if (product.getProductQuantity() < quantity) {
            throw new APIException("Product quantity less than ordered quantity");
        }

        if (product.getProductQuantity() == 0) {
            throw new APIException("Product " + product.getProductName() + " is not available");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setCart(cart);
        newCartItem.setDiscount(product.getDiscountPrice());
        newCartItem.setProductPrice(product.getProductPrice());

        cartItemRepository.save(newCartItem);

        product.setProductQuantity(product.getProductQuantity());
       ///productRepository.save(product);

        cart.setTotalPrice(product.getProductPrice() * product.getProductQuantity());
        cart.getCartItems().add(newCartItem);
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setProductQuantity(item.getQuantity());
            return map;
        });
        cartDTO.setProducts(productStream.toList());
        return cartDTO;
    }

    private Cart createCart() {
        Cart newUserCart = cartRepository.findCartByEmail(authUtil.loggedEmail());
        if (newUserCart == null) {
            newUserCart = new Cart();
            newUserCart.setTotalPrice(0.00);
            newUserCart.setUser(authUtil.loggedInUser());
            cartRepository.save(newUserCart);
            return newUserCart;
        }
        return newUserCart;
    }
}
