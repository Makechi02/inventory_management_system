package entity;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private String description;
    private int quantity;
    private double price;

    public Item(String name, String description, int quantity, double price) {
        this.name = name;
        this.description = description;
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
        System.out.println("Quantity: " + getQuantity());
        System.out.println("Price: " + getPrice() + "\n");
    }

    @Override
    public String toString() {
        return "Item {" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
