package com.makechi.inventory_management_system.service.category;

import com.makechi.inventory_management_system.connections.Connections;
import com.makechi.inventory_management_system.entity.Category;
import com.makechi.inventory_management_system.exception.DuplicateResourceException;
import com.makechi.inventory_management_system.exception.RequestValidationException;
import com.makechi.inventory_management_system.exception.ResourceNotFoundException;

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
            throw new ResourceNotFoundException("Category with name: " + name + " not found");
        }

        return connections.updateCategory(fetchedCategory.getId(), category.getName());
    }

    @Override
    public int updateCategory(int id, String name) {
        Category fetchedCategory = connections.getCategoryById(id);
        if (fetchedCategory == null) {
            throw new ResourceNotFoundException("Category with id: " + id + " not found");
        }

        boolean changes = false;

        if (!name.isBlank() && !name.equals(fetchedCategory.getName())) {
            if (connections.categoryExistsByName(name))
                throw new DuplicateResourceException("Category already exist");
            changes = true;
        }

        if (!changes) throw new RequestValidationException("No changes made");

        return connections.updateCategory(fetchedCategory.getId(), name);
    }

    @Override
    public int deleteCategory(String name) {
        if (!connections.categoryExistsByName(name)) {
            throw new RuntimeException("Category with name: " + name + " not found");
        }
        return connections.deleteCategory(name);
    }
}
