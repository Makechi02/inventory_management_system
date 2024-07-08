package service.category;

import connections.Connections;
import entity.Category;
import exception.RequestValidationException;
import exception.DuplicateResourceException;
import exception.ResourceNotFoundException;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final Connections connections = Connections.getInstance();

    @Override
    public Category getCategoryByName(String name) {
        Category category = connections.getCategoryByName(name);
        if (category == null) throw new ResourceNotFoundException("Category with name: " + name + " not found");
        return category;
    }

    @Override
    public Category getCategoryById(int id) {
        Category category = connections.getCategoryById(id);
        if (category == null) throw new ResourceNotFoundException("Category with id: " + id + " not found");
        return category;
    }

    @Override
    public List<Category> getAllCategories() {
        return connections.getAllCategories();
    }

    @Override
    public int saveCategory(Category category) {
        if (connections.categoryExistsByName(category.getName()))
            throw new DuplicateResourceException("Category already exists");
        return connections.saveCategory(category);
    }

    @Override
    public int updateCategory(String name, Category category) {
        Category fetchedCategory = connections.getCategoryByName(name);
        if (fetchedCategory == null) {
            throw new RuntimeException("Category with name: " + name + " not found");
            throw new ResourceNotFoundException("Category with name: " + name + " not found");
        }
        return connections.updateCategory(fetchedCategory.getId(), category.getName());
    }

    @Override
    public int deleteCategory(String name) {
        if (!connections.categoryExistsByName(name)) {
            throw new RuntimeException("Category with name: " + name + " not found");
        }
        return connections.deleteCategory(name);
    }
}
