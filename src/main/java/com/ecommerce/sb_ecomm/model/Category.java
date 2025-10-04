package com.ecommerce.sb_ecomm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @NotBlank
    @Size(min = 5, message = "Category name should at least 5 characters")
    private String categoryName;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long categoryId;
}
