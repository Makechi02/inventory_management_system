package entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Inventory implements Serializable {
    private final List<Item> items;
    private final List<User> users;

    public Inventory() {
        items = new ArrayList<>();
        users = new ArrayList<>();
        populateItemsList();
        populateUsersList();
    }

    public static void saveToFile(Inventory inventory, String filename) {
        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(inventory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Inventory loadFromFile(String filename) {
        Inventory inventory;
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            inventory = (Inventory) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            inventory = new Inventory();
        }

        return inventory;
    }

    private void populateItemsList() {
        items.add(new Item("Laptop", "Dell Inspiron 15", 10, 7000));
        items.add(new Item("Smartphone", "IPhone 12", 5, 10000));
        items.add(new Item("Mouse", "Logitech Wireless Mouse", 20, 300));
        items.add(new Item("Keyboard", "Mechanical Gaming Keyboard", 15, 800));
        items.add(new Item("Headphones", "Sony Noise-Cancelling Headphone", 8, 2000));
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void updateItem(int index, Item item) {
        if (index >= 0 && index < items.size()) {
            items.set(index, item);
        }
    }

    public Item deleteItem(int index) {
        Item item = null;
        if (index >= 0 && index < items.size()) {
            item = items.remove(index);
        }
        return item;
    }

    public Item getItem(int index) {
        return items.get(index);
    }

    public List<Item> getItems() {
        return items;
    }

    private void populateUsersList() {
        users.add(new User("Admin", "admin", UserType.ADMIN));
        users.add(new User("User", "password", UserType.REGULAR));
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void updateUser(int index, User user) {
        if (index >= 0 && index < users.size()) {
            users.set(index, user);
        }
    }

    public User deleteUser(int index) {
        User user = null;
        if (index >= 0 && index < users.size()) {
            user = users.remove(index);
        }
        return user;
    }

    public User getUser(int index) {
        return users.get(index);
    }

    public List<User> getUsers() {
        return users;
    }
}
