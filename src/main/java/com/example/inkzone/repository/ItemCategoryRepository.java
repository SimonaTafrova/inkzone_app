package com.example.inkzone.repository;

import com.example.inkzone.model.entity.ItemCategory;
import com.example.inkzone.model.enums.ItemCategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

    Optional<ItemCategory> getItemCategoryByName(ItemCategoryEnum itemCategoryEnum);
}
