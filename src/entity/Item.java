package entity;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

public class Item implements Serializable {
    private int id;
    private String name;
    private String brand;
    private String model;
    private String sku;
    private int quantity;
    private double price;
    private Category category;

    public Item(String name, String brand, String model, String sku, int quantity, double price, Category category) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.sku = sku;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void display() {
        System.out.println("\nITEM DETAILS");
        System.out.println("Name: " + getName());
        System.out.println("Brand: " + getName());
        System.out.println("Model: " + getModel());
        System.out.println("SKU: " + getSku());
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
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", sku='" + sku + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", category=" + category +
                '}';
    }
}
