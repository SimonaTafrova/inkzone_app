package com.example.inkzone.model.dto.binding;

import com.example.inkzone.model.entity.ItemCategory;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class ItemAddBindingModel {


    @NotBlank
    @NotNull
    private String name;

    @NotNull
    @PositiveOrZero
    private Integer quantity;
    @NotNull
    @PositiveOrZero
    private Integer minQuantity;

    @NotNull
    private String category;

    public ItemAddBindingModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
