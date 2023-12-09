package com.example.inkzone.service.impl;

import com.example.inkzone.model.entity.Item;
import com.example.inkzone.model.entity.Picture;
import com.example.inkzone.repository.ItemRepository;
import com.example.inkzone.repository.PictureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class PictureDeleteService {
    private final PictureRepository pictureRepository;


    public PictureDeleteService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;

    }

    public void getAllPicturesOfUserAndDelete(String email) {
        List<Picture> picturesToDelete = pictureRepository.getAllByCreator_Email(email).orElse(null);
        if(picturesToDelete != null){
            pictureRepository.deleteAll(picturesToDelete);
        }

    }








}
