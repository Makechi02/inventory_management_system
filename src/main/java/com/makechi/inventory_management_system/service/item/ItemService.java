package com.makechi.inventory_management_system.service.item;

import com.makechi.inventory_management_system.entity.Category;
import com.makechi.inventory_management_system.entity.Item;

import java.util.List;

public interface ItemService {
    int saveItem(Item item);
    Item getItemBySku(String sku);
    List<Item> getAllItems();
    int updateItem(String sku, Item item);
    int deleteItem(String sku);
    List<Item> filterByCategory(Category category);
    List<Item> filterByPriceRange(double minPrice, double maxPrice);
    List<Item> searchItemsByName(String name);
}

