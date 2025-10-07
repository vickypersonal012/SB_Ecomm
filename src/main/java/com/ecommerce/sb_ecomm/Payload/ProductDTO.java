package com.ecommerce.sb_ecomm.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private long productId;
    private String productName;
    private String productDescription;
    private double productPrice;
    private Integer productQuantity;
    private String specialPrice;
    private String productImageUrl;

}
