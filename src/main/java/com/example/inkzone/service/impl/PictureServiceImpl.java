package com.example.inkzone.service.impl;

import com.example.inkzone.model.dto.binding.PictureAddBindingModel;
import com.example.inkzone.model.dto.view.ItemViewModel;
import com.example.inkzone.model.dto.view.PictureGalleryViewModel;
import com.example.inkzone.model.entity.Item;
import com.example.inkzone.model.entity.Picture;
import com.example.inkzone.model.entity.UserEntity;
import com.example.inkzone.model.enums.ItemCategoryEnum;
import com.example.inkzone.repository.PictureRepository;
import com.example.inkzone.service.ItemService;
import com.example.inkzone.service.PictureService;
import com.example.inkzone.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ItemService itemService;

    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper, UserService userService, ItemService itemService) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public List<PictureGalleryViewModel> getAllPicturesByUserId(String name) {
        List<PictureGalleryViewModel> pictureGalleryViewModels = new ArrayList<>();
        List<Picture> all = pictureRepository.getAllByCreator_Email(name).orElse(null);
        assert all != null;
        all.forEach(p -> {
            PictureGalleryViewModel pictureGalleryViewModel = modelMapper.map(p, PictureGalleryViewModel.class);
            pictureGalleryViewModels.add(pictureGalleryViewModel);
        });

        return pictureGalleryViewModels;
    }

    @Override
    public void savePicture(PictureAddBindingModel pictureAddBindingModel, String name) {
        UserEntity user = userService.getCreator(name);
        if(user == null){
            throw new RuntimeException("Session expired. Please log in again!");
        }
        Picture picture = modelMapper.map(pictureAddBindingModel, Picture.class);
        picture.setCreator(user);
        Set<String> items = new HashSet<>();
        pictureAddBindingModel.getMaterialsUsed()
                        .forEach(m -> {
                            String item = itemService.getAndUpdateItem(m);
                            items.add(item);
                        });
        picture.setMaterialsUsed(items);

        pictureRepository.save(picture);


    }

    @Override
    public PictureGalleryViewModel getPictureById(Long id) {
        Picture picture = pictureRepository.findById(id).orElse(null);
        if(picture == null){
            throw new RuntimeException("Picture not found!");
        }
        PictureGalleryViewModel pictureGalleryViewModel = modelMapper.map(picture, PictureGalleryViewModel.class);
        List<String> materials = picture.getMaterialsUsed().stream().toList();
        pictureGalleryViewModel.setMaterialsUsed(materials);
        return pictureGalleryViewModel;
    }

    @Override
    public PictureGalleryViewModel deletePictureById(Long id) {
        Picture picture = pictureRepository.findById(id).orElse(null);
        if(picture == null){
            throw new RuntimeException("Picture not found");
        }
        PictureGalleryViewModel pictureGalleryViewModel = modelMapper.map(picture, PictureGalleryViewModel.class);
        pictureRepository.delete(picture);
        return pictureGalleryViewModel;
    }

    @Override
    public void getAllPicturesOfUserAndDelete(String email) {
        List<Picture> picturesToDelete = pictureRepository.getAllByCreator_Email(email).orElse(null);
        if(picturesToDelete != null){
            pictureRepository.deleteAll(picturesToDelete);
        }
    }

    @Override
    public List<ItemViewModel> getAllItems(ItemCategoryEnum itemCategoryEnum) {
        List<ItemViewModel> inStockList = new ArrayList<>();
        itemService.getAllItems(itemCategoryEnum)
                .forEach(item -> {
                    if(item.getQuantity() > 0 ){
                        inStockList.add(item);
                    }
                });

        return inStockList;

    }
}
