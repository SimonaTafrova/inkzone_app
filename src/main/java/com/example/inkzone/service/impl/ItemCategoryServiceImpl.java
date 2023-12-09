package com.example.inkzone.service.impl;

import com.example.inkzone.model.entity.ItemCategory;
import com.example.inkzone.model.enums.ItemCategoryEnum;
import com.example.inkzone.repository.ItemCategoryRepository;
import com.example.inkzone.service.ItemCategoryService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {
    private final ItemCategoryRepository itemCategoryRepository;

    public ItemCategoryServiceImpl(ItemCategoryRepository itemCategoryRepository) {
        this.itemCategoryRepository = itemCategoryRepository;
    }

    @Override
    public void init() {
        if(itemCategoryRepository.count() == 0){
            Arrays.stream(ItemCategoryEnum.values()).forEach(itemCategoryEnum -> {
                ItemCategory itemCategory = new ItemCategory();
                itemCategory.setName(itemCategoryEnum);
                itemCategoryRepository.save(itemCategory);
            });
        }

    }

    @Override
    public ItemCategory getCategory(ItemCategoryEnum itemCategoryEnum) {
        return itemCategoryRepository.getItemCategoryByName(itemCategoryEnum).orElse(null);
    }
}
