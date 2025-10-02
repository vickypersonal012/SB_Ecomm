package com.ecommerce.sb_ecomm.Service;

import com.ecommerce.sb_ecomm.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public List<Category> getCategoryList() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
       Category category = categories.stream().filter(c -> c.getCategoryId() == categoryId).findFirst().orElse(null);
       if(category==null){
           return "Category Not found";
       }
        categories.remove(category);
        return "category with category id" + categoryId +" deleted successfully";
    }
}
