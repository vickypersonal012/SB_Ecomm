package com.ecommerce.sb_ecomm.Service;

import com.ecommerce.sb_ecomm.model.Category;
import com.ecommerce.sb_ecomm.repositories.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
   // private List<Category> categories = new ArrayList<>();
   // private Long nextId = 1L;

    public CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        //category.setCategoryId(nextId++);
        categoryRepository.save(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        Optional<Category> optionalSavedCategory = categoryRepository.findById(categoryId);
        Category savedCategory = optionalSavedCategory.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        categoryRepository.delete(savedCategory);
        return "category with category id" + categoryId +" deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> optionalSavedCategory = categoryRepository.findById(categoryId);
        Category savedCategory = optionalSavedCategory.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        category.setCategoryId(categoryId);
        return categoryRepository.save(category);
    }
}
