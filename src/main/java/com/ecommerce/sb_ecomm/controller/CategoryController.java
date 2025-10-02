package com.ecommerce.sb_ecomm.controller;

import com.ecommerce.sb_ecomm.Service.CategoryService;
import com.ecommerce.sb_ecomm.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.service.annotation.DeleteExchange;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    public CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

   // @GetMapping("/public/categories")
    @RequestMapping(value = "/public/categories" , method =  RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getCategoryList();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@RequestBody Category category){
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category has been added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        try {
            String status = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        }catch (ResponseStatusException ex){
            return new ResponseEntity<>(ex.getReason(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryId){
        try{
            Category savedCategory = categoryService.updateCategory(category, categoryId);
            return new ResponseEntity<>("Category id "+ categoryId + " has been updated successfully", HttpStatus.OK);
        }catch (ResponseStatusException ex){
            return new ResponseEntity<>(ex.getReason(), HttpStatus.NOT_FOUND);
        }

    }


}
