package com.example.inkzone.service;

import com.example.inkzone.model.entity.ItemCategory;
import com.example.inkzone.model.enums.ItemCategoryEnum;

public interface ItemCategoryService {
    void init();

    ItemCategory getCategory(ItemCategoryEnum itemCategoryEnum);
}
