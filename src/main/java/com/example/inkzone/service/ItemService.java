package com.example.inkzone.service;

import com.example.inkzone.model.dto.binding.ItemAddBindingModel;
import com.example.inkzone.model.dto.view.ItemViewModel;
import com.example.inkzone.model.enums.ItemCategoryEnum;

import java.util.List;

public interface ItemService {


    long addItem(ItemAddBindingModel itemAddBindingModel);

    ItemViewModel getItemViewModel(long newItemId);

    List<ItemViewModel> getAllItems(ItemCategoryEnum needle);

    ItemViewModel deleteItem(Long id);

    ItemViewModel editItem(Long id, ItemAddBindingModel itemAddBindingModel);

    String getAndUpdateItem(String m);

    List<ItemViewModel> getLowQuantityItems();
}
