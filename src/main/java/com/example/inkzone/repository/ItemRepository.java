package com.example.inkzone.repository;

import com.example.inkzone.model.entity.Item;
import com.example.inkzone.model.entity.ItemCategory;
import com.example.inkzone.model.enums.ItemCategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByCategory(ItemCategory itemCategory);

    Item findByName(String name);

}
