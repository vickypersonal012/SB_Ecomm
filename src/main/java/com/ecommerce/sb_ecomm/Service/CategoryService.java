package com.ecommerce.sb_ecomm.Service;

import com.ecommerce.sb_ecomm.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getCategoryList();
    void createCategory(Category category);

    String deleteCategory(Long categoryId);

    Category updateCategory(Category category, Long categoryId);
}
