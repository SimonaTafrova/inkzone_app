package com.example.inkzone.service;

import com.example.inkzone.model.dto.binding.UserRegisterBingingModel;
import com.example.inkzone.model.dto.view.UserViewModel;
import com.example.inkzone.model.entity.UserEntity;

import java.util.List;

public interface UserService {
    void registerUser(UserRegisterBingingModel userRegisterBingingModel);

    UserViewModel getUserByUserName(String name);



    void editFirstName(String name, String firstName, String password);

    void editLastName(String name, String lastName, String password);


    void editEmail(String name, String email, String password);

    void editPassword(String newPassword, String newPasswordConfirm, String oldPasswordConfirm, String name);

    String getFullName(String name);

    void addProfilePicture(String name, String imageURL);

    String getProfilePicture(String name);

    String getUserId(String name);


    UserEntity getCreator(String name);

    List<UserViewModel> getAllNonAdminUsers(String name);

    void deleteUser(Long id);

    void makeModerator(Long id);

    void removeModeration(Long id);


    String[] getAllAdminsAndModerators();

   void updatePassword(UserViewModel user, String newPassword);

     UserViewModel getByResetPasswordToken(String token);
    void updateResetPasswordToken(String token, String email);


}
