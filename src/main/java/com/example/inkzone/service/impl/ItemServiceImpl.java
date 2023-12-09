package com.example.inkzone.service.impl;

import com.example.inkzone.model.dto.binding.ItemAddBindingModel;
import com.example.inkzone.model.dto.view.ItemViewModel;
import com.example.inkzone.model.entity.Item;
import com.example.inkzone.model.entity.ItemCategory;
import com.example.inkzone.model.enums.ItemCategoryEnum;
import com.example.inkzone.repository.ItemRepository;
import com.example.inkzone.service.ItemCategoryService;
import com.example.inkzone.service.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final ItemCategoryService itemCategoryService;



    public ItemServiceImpl(ItemRepository itemRepository, ModelMapper modelMapper, ItemCategoryService itemCategoryService) {
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
        this.itemCategoryService = itemCategoryService;

    }

    @Override
    public List<ItemViewModel> getAllItems(ItemCategoryEnum itemCategoryEnum) {
        ItemCategory itemCategory = itemCategoryService.getCategory(itemCategoryEnum);

        return itemRepository.findAllByCategory(itemCategory)
                .stream()
                .map(i -> modelMapper.map(i,ItemViewModel.class))
                .collect(Collectors.toList());

    }

    @Override
    public ItemViewModel deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item was not found!"));
        itemRepository.delete(item);
        return modelMapper.map(item, ItemViewModel.class);




    }

    @Override
    public ItemViewModel editItem(Long id, ItemAddBindingModel itemAddBindingModel) {
        Item itemToEdit = itemRepository.findById(id).orElse(null);
        if(itemToEdit == null){
            throw new RuntimeException("Item not found!");
        }

        if(itemRepository.findByName(itemAddBindingModel.getName()) != null && itemRepository.findByName(itemAddBindingModel.getName()).getId() != id){
            throw new RuntimeException("Item with this name is already present!");
        }

        itemToEdit.setName(itemAddBindingModel.getName());
        itemToEdit.setQuantity(itemAddBindingModel.getQuantity());
        itemToEdit.setMinQuantity(itemAddBindingModel.getMinQuantity());

        ItemCategory itemCategory = itemCategoryService.getCategory(ItemCategoryEnum.valueOf(itemAddBindingModel.getCategory()));
        itemToEdit.setCategory(itemCategory);
        itemRepository.save(itemToEdit);

        return modelMapper.map(itemToEdit, ItemViewModel.class);
    }

    @Override
    public String getAndUpdateItem(String m) {
        Item item = itemRepository.findByName(m);
        item.setQuantity(item.getQuantity() - 1);
        itemRepository.save(item);

        return item.getName();
    }

    @Override
    public List<ItemViewModel> getLowQuantityItems() {
        List<ItemViewModel> lowItems = new ArrayList<>();
        itemRepository.findAll().forEach(i -> {
            if(i.getQuantity() <= i.getMinQuantity()){
                ItemViewModel itemViewModel = modelMapper.map(i, ItemViewModel.class);
                lowItems.add(itemViewModel);
            }
        });
        return lowItems;
    }

    @Override
    public long addItem(ItemAddBindingModel itemAddBindingModel) {

        if(itemRepository.findByName(itemAddBindingModel.getName()) != null){
            throw new RuntimeException("Item with this name is already present!");
        }

        Item itemToAdd = modelMapper.map(itemAddBindingModel,Item.class);
        ItemCategory itemCategory = itemCategoryService.getCategory(ItemCategoryEnum.valueOf(itemAddBindingModel.getCategory()));
        itemToAdd.setCategory(itemCategory);
        itemRepository.save(itemToAdd);
        Item item = itemRepository.findByName(itemToAdd.getName());


        return item.getId();


    }

    @Override
    public ItemViewModel getItemViewModel(long newItemId) {
        Item item = itemRepository.findById(newItemId).orElse(null);
        return modelMapper.map(item, ItemViewModel.class);
    }


}
