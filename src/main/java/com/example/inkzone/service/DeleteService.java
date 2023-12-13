package com.example.inkzone.service;

import com.example.inkzone.model.entity.UserEntity;

public interface DeleteService {

    public void getAllPicturesOfUserAndDelete(String email);

    public void deleteEvents(UserEntity userEntity);

}
