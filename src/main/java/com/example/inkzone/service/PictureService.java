package com.example.inkzone.service;

import com.example.inkzone.model.dto.binding.PictureAddBindingModel;
import com.example.inkzone.model.dto.view.ItemViewModel;
import com.example.inkzone.model.dto.view.PictureGalleryViewModel;
import com.example.inkzone.model.entity.Picture;
import com.example.inkzone.model.enums.ItemCategoryEnum;

import java.util.List;

public interface PictureService {
    List<PictureGalleryViewModel> getAllPicturesByUserId(String name);

    void savePicture(PictureAddBindingModel pictureAddBindingModel, String name);

    PictureGalleryViewModel getPictureById(Long id);

    PictureGalleryViewModel deletePictureById(Long id);


    void getAllPicturesOfUserAndDelete(String email);

    List<ItemViewModel> getAllItems(ItemCategoryEnum itemCategoryEnum);
}
