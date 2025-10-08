package com.ecommerce.sb_ecomm.Service;

import com.ecommerce.sb_ecomm.Payload.CategoryDTO;
import com.ecommerce.sb_ecomm.Payload.CategoryResponse;

public interface CategoryService {

    CategoryResponse getCategoryList(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
