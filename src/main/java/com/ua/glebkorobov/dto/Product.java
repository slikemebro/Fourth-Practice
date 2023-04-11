package com.ua.glebkorobov.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Product {

    private int address;

    private int type;

    private int quantity;

    private String name;

    private boolean valid = true;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Product() {
    }

    public Product(int address, int type, int quantity, String name) {
        this.address = address;
        this.type = type;
        this.quantity = quantity;
        this.name = name;
    }

    @Max(value = 9, message = "Value more than possible")
    @Min(value = 1, message = "Value less than possible")
    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    @Max(value = 100, message = "Value more than possible")
    @Min(value = 1, message = "Value less than possible")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Max(value = 300, message = "Value more than possible")
    @Min(value = 1, message = "Value less than possible")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @NotBlank(message = "Element must contain characters and mustn't be null")
    @Size(min = 3, max = 30, message = "Incorrect size of name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
