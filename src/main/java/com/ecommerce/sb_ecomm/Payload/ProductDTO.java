package com.ecommerce.sb_ecomm.Payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private long productId;
    @NotBlank
    @Size(min = 3, message = "Product Name should contain at least 3 characters ")
    private String productName;
    @NotBlank
    @Size(min = 6, max =100, message = "Product Description should contain at least 6 characters ")
    private String productDescription;
    private double productPrice;
    private Integer productQuantity;
    private String specialPrice;
    private double discountPrice;
    private String productImageUrl;
}
