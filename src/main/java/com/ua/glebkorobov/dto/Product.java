package com.ua.glebkorobov.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Product {

    private int type;

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

    public Product(String name, int type) {
        this.type = type;
        this.name = name;
    }

    @Max(value = 100, message = "Value more than possible")
    @Min(value = 1, message = "Value less than possible")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @NotBlank(message = "Element must contain characters and mustn't be null")
    @Size(min = 3, max = 30, message = "Incorrect size of name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", valid=" + valid +
                '}';
    }
}
