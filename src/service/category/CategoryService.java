package service.category;

import entity.Category;

import java.util.List;

public interface CategoryService {
    Category getCategoryByName(String name);
    Category getCategoryById(int id);
    List<Category> getAllCategories();
    int saveCategory(Category category);
    int updateCategory(String name, Category category);
    int deleteCategory(String name);
}
