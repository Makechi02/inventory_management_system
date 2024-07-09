package com.makechi.inventory_management_system.service.category;


import com.makechi.inventory_management_system.entity.Category;

import java.util.List;

public interface CategoryService {
    Category getCategoryByName(String name);
    Category getCategoryById(int id);
    List<Category> getAllCategories();
    int saveCategory(Category category);
    int updateCategory(String name, Category category);
    int updateCategory(int id, String name);
    int deleteCategory(String name);
}
