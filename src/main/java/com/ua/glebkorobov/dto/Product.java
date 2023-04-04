package com.ua.glebkorobov.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class Product {

    private int address;

    private int name;

    private int quantity;

    private boolean valid = true;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Product(int address, int name, int quantity) {
        this.address = address;
        this.name = name;
        this.quantity = quantity;
    }

    public Product() {
    }

    @Min(value = 1)
    @Max(value = 9)
    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    @Min(value = 1)
    @Max(value = 500)
    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    @Min(value = 1)
    @Max(value = 300)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "address=" + address +
                ", name=" + name +
                ", quantity=" + quantity +
                '}';
    }
}
