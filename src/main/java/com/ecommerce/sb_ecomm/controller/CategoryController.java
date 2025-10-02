package com.ecommerce.sb_ecomm.controller;

import com.ecommerce.sb_ecomm.Service.CategoryService;
import com.ecommerce.sb_ecomm.model.Category;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.DeleteExchange;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    public CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories")
    public List<Category> getAllCategories(){
        return categoryService.getCategoryList();
    }

    @PostMapping("/api/public/categories")
    public String createCategory(@RequestBody Category category){
        categoryService.createCategory(category);
        return "Category Added Successfully";
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId){
        String status = categoryService.deleteCategory(categoryId);
        return status;

    }


}
