package com.ecommerce.sb_ecomm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "categories")
public class Category {
    private String categoryName;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long categoryId;

    public Category(long categoryId, String categoryName) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }

    public Category() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
