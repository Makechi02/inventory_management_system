package com.makechi.inventory_management_system.service.item;

import com.makechi.inventory_management_system.entity.Item;

public class SKUGenerator {
    public static String generateSKU(Item item, int id) {
        String categoryPrefix = getCategoryPrefix(item.getCategory().getName());
        String namePrefix = getNamePrefix(item.getName());
        return categoryPrefix + "-" + namePrefix + "-" + String.format("%04d", id);
    }

    private static String getCategoryPrefix(String category) {
        if (category == null || category.isEmpty()) return "UNK";
        return category.substring(0, Math.min(category.length(), 3)).toUpperCase();
    }

    private static String getNamePrefix(String name) {
        if (name == null || name.isEmpty()) return "UNK";
        return name.replaceAll("\\s+", "").substring(0, Math.min(name.length(), 3)).toUpperCase();
    }
}
