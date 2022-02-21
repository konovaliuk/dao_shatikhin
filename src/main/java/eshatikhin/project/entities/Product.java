package eshatikhin.project.entities;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {
    private int id;
    private String name;
    private float weight;
    private double price;
    private float qty_instock;

    public Product() {
    }

    public Product(int id, String name, float weight, double price, float qty_instock) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.price = price;
        this.qty_instock = qty_instock;
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public float getQty_instock() {
        return qty_instock;
    }

    public void setQty_instock(float qty_instock) {
        this.qty_instock = qty_instock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return getName().equals(product.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
