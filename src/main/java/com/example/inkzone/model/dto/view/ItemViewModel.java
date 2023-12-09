package com.example.inkzone.model.dto.view;

import com.example.inkzone.model.entity.ItemCategory;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

public class ItemViewModel {


    private String name;

    private Integer quantity;

    private Integer minQuantity;
    private Long id;




    public ItemViewModel() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
