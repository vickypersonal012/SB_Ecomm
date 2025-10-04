package com.ecommerce.sb_ecomm.Service;

import com.ecommerce.sb_ecomm.Payload.CategoryDTO;
import com.ecommerce.sb_ecomm.Payload.CategoryResponse;
import com.ecommerce.sb_ecomm.model.Category;

import java.util.List;

public interface CategoryService {

    CategoryResponse getCategoryList(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
