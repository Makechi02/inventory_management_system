package entity;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

public class Item implements Serializable {
    private String name;
    private String description;
    private Category category;
    private int quantity;
    private double price;

    public Item(String name, String description, Category category, int quantity, double price) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void display() {
        System.out.println("\nITEM DETAILS");
        System.out.println("Name: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Category: " + getCategory());
        System.out.println("Quantity: " + getQuantity());
        System.out.println("Price: " + getFormattedPrice() + "\n");
    }

    public String getFormattedPrice() {
        Locale kenya = new Locale("en", "KE");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(kenya);
        return numberFormat.format(getPrice());
    }

    @Override
    public String toString() {
        return "Item {" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", quantity=" + quantity +
                ", price=" + getFormattedPrice() +
                '}';
    }
}
