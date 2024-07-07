package service.item;

import connections.Connections;
import entity.Category;
import entity.Item;

import java.util.List;

public class ItemServiceImpl implements ItemService {
    private final Connections connections = Connections.getInstance();

    @Override
    public int saveItem(Item item) {
        if (connections.ItemExistsBySku(item.getSku()))
            throw new RuntimeException("SKU already exists");
        return connections.saveItem(item);
    }

    @Override
    public Item getItemBySku(String sku) {
        Item item = connections.getItemBySku(sku);
        if (item == null)
            throw new RuntimeException("Item with sku: " + sku + " not found");
        return item;
    }

    @Override
    public List<Item> getAllItems() {
        return connections.getAllItems();
    }

    @Override
    public int updateItem(String sku, Item item) {
        Item fetchedItem = getItemBySku(sku);
        if (fetchedItem == null)
            throw new RuntimeException("Item with sku: " + sku + " not found");

        return connections.updateItem(sku, item);
    }

    @Override
    public int deleteItem(String sku) {
        if (!connections.ItemExistsBySku(sku))
            throw new RuntimeException("Item with sku: " + sku + " not found");
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
