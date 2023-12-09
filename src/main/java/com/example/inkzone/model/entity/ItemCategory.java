package com.example.inkzone.model.entity;


import com.example.inkzone.model.enums.ItemCategoryEnum;

import javax.persistence.*;

@Entity
@Table(name = "item_categories")
public class ItemCategory extends BaseEntity{

    @Enumerated(EnumType.STRING)
    public ItemCategoryEnum name;

    public ItemCategory() {
    }

    public ItemCategoryEnum getName() {
        return name;
    }

    public void setName(ItemCategoryEnum name) {
        this.name = name;
    }
}
