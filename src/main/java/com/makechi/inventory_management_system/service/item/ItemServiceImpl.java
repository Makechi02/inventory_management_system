package com.makechi.inventory_management_system.service.item;

import com.makechi.inventory_management_system.connections.Connections;
import com.makechi.inventory_management_system.entity.Category;
import com.makechi.inventory_management_system.entity.Item;
import com.makechi.inventory_management_system.exception.DuplicateResourceException;
import com.makechi.inventory_management_system.exception.RequestValidationException;
import com.makechi.inventory_management_system.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Objects;

public class ItemServiceImpl implements ItemService {
    private final Connections connections = Connections.getInstance();

    @Override
    public int saveItem(Item item) {
        if (connections.ItemExistsBySku(item.getSku()))
            throw new DuplicateResourceException("SKU already exists");

        if (item.getSku() == null) {
            String sku = SKUGenerator.generateSKU(item, connections.getNextItemId());
            item.setSku(sku);
        }

        return connections.saveItem(item);
    }

    @Override
    public Item getItemBySku(String sku) {
        Item item = connections.getItemBySku(sku);
        if (item == null)
            throw new ResourceNotFoundException("Item with sku: " + sku + " not found");
        return item;
    }

    @Override
    public List<Item> getAllItems() {
        return connections.getAllItems();
    }

    @Override
    public int updateItem(String sku, Item item) {
        boolean changes = false;
        Item fetchedItem = getItemBySku(sku);
        if (fetchedItem == null)
            throw new ResourceNotFoundException("Item with sku: " + sku + " not found");

        if (!item.getName().isBlank() && !item.getName().equals(fetchedItem.getName())) {
            changes = true;
        }

        if (!item.getBrand().isBlank() && !item.getBrand().equals(fetchedItem.getBrand())) {
            changes = true;
        }

        if (!item.getModel().isBlank() && !item.getModel().equals(fetchedItem.getModel())) {
            changes = true;
        }

        if (item.getQuantity() != fetchedItem.getQuantity()) {
            changes = true;
        }

        if (item.getPrice() != fetchedItem.getPrice()) {
            changes = true;
        }

        if (item.getCategory() != null && !Objects.equals(item.getCategory().getName(), fetchedItem.getCategory().getName())) {
            changes = true;
        }

        if (!changes) throw new RequestValidationException("No changes found");

        return connections.updateItem(sku, item);
    }

    @Override
    public int deleteItem(String sku) {
        if (!connections.ItemExistsBySku(sku))
            throw new ResourceNotFoundException("Item with sku: " + sku + " not found");
        return connections.deleteItem(sku);
    }

    @Override
    public List<Item> filterByCategory(Category category) {
        List<Item> items = getAllItems();
        return items.stream().filter(item -> item.getCategory() == category).toList();
    }

    @Override
    public List<Item> filterByPriceRange(double minPrice, double maxPrice) {
        List<Item> items = getAllItems();
        return items.stream().filter(item -> item.getPrice() >= minPrice && item.getPrice() <= maxPrice).toList();
    }

    @Override
    public List<Item> searchItemsByName(String name) {
        return connections.searchItemsByName(name);
    }
}
