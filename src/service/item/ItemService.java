package service.item;

import entity.Category;
import entity.Item;

import java.util.List;

public interface ItemService {
    int saveItem(Item item);
    Item getItemBySku(String sku);
    List<Item> getAllItems();
    int updateItem(String sku, Item item);
    int deleteItem(String sku);
    List<Item> filterByCategory(Category category);
    List<Item> filterByPriceRange(double minPrice, double maxPrice);
}
