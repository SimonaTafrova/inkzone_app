package com.example.inkzone.service.impl;

import com.example.inkzone.model.dto.binding.UserRegisterBingingModel;
import com.example.inkzone.model.dto.view.UserViewModel;
import com.example.inkzone.model.entity.Role;
import com.example.inkzone.model.entity.UserEntity;
import com.example.inkzone.model.enums.RoleEnum;
import com.example.inkzone.repository.UserRepository;
import com.example.inkzone.service.EmailService;
import com.example.inkzone.service.RoleService;
import com.example.inkzone.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final EmailService emailService;
    private final PictureDeleteService pictureService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper,
                           RoleService roleService,
                           EmailService emailService,
                           PictureDeleteService pictureService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.emailService = emailService;

        this.pictureService = pictureService;
    }

    @Override
    public void registerUser(UserRegisterBingingModel userRegisterBingingModel) {
        if(!userRegisterBingingModel.getPassword().equals(userRegisterBingingModel.getConfirmPassword())){
            throw new RuntimeException("passwords.match");
        }

        Optional <UserEntity> byEmail = userRepository.findByEmail(userRegisterBingingModel.getEmail());

        if(byEmail.isPresent()){
            throw new RuntimeException("email.used");
        }

        UserEntity userEntity = modelMapper.map(userRegisterBingingModel, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userRegisterBingingModel.getPassword()));
        userEntity.setProfilePicture("http://res.cloudinary.com/dycdo8t3a/image/upload/v1698319707/5fd5598e-97fb-48e3-a553-c394e0b88589.webp");

        setRoles(userEntity);

        userRepository.save(userEntity);
        emailService.sendRegistrationEmail(modelMapper.map(userRegisterBingingModel, UserViewModel.class));







    }

    private void setRoles(UserEntity userEntity) {

        if (userRepository.count() == 0){
            Role adminRole = roleService.getRoleByName(RoleEnum.ADMIN);
            Role moderatorRole = roleService.getRoleByName(RoleEnum.MODERATOR);
            Role userRole = roleService.getRoleByName(RoleEnum.USER);

            Set <Role> roles = new HashSet<>();
            roles.add(userRole);
            roles.add(adminRole);
            roles.add(moderatorRole);
            userEntity.setRoles(roles);
        } else {
            Role userRole = roleService.getRoleByName(RoleEnum.USER);

            Set <Role> roles = new HashSet<>();
            roles.add(userRole);
            userEntity.setRoles(roles);
        }
    }

    @Override
    public UserViewModel getUserByUserName(String name) {
        UserEntity currentUser = userRepository.findByEmail(name).orElse(null);

        return modelMapper.map(currentUser, UserViewModel.class);
    }


    @Override
    public void editFirstName(String name, String firstName, String password) {
        UserEntity userEntity = userRepository.findByEmail(name).orElse(null);

        assert userEntity != null;
        if(passwordEncoder.matches(password,userEntity.getPassword())){
            userEntity.setFirstName(firstName);
        } else{
            throw new RuntimeException("Wrong password");
        }

        userRepository.save(userEntity);

    }

    @Override
    public void editLastName(String name, String lastName, String password) {
        UserEntity userEntity = userRepository.findByEmail(name).orElseThrow(() -> new RuntimeException("User not found!"));


        if(passwordEncoder.matches(password,userEntity.getPassword())){
            userEntity.setLastName(lastName);
        } else {
            throw new RuntimeException("wrong password");
        }

        userRepository.save(userEntity);

    }

    @Override
    public void editEmail(String name, String email, String password) {
        UserEntity user = userRepository.findByEmail(name).orElseThrow(() -> new RuntimeException("User not found!"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("wrong password");
        }

        Optional <UserEntity> byEmail = userRepository.findByEmail(email);
        if(byEmail.isPresent()){
            throw new RuntimeException("email used");
        }

        setNewAuthentication(email,password);



        user.setEmail(email);
        userRepository.save(user);





    }

    @Override
    public void editPassword(String newPassword,
                             String newPasswordConfirm,
                             String oldPasswordConfirm, String name) {
        UserEntity userEntity = userRepository.findByEmail(name).orElseThrow(() -> new RuntimeException("User not found!"));
        if(!passwordEncoder.matches(oldPasswordConfirm, userEntity.getPassword())){
            throw new RuntimeException("old password mismatch");
        }

        if(!newPassword.equals(newPasswordConfirm)){
            throw  new RuntimeException("password mismatch");
        }

        setNewAuthentication(name, newPassword);



        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
    }

    @Override
    public String getFullName(String name) {

        UserEntity userEntity = userRepository.findByEmail(name).orElse(null);
        if(userEntity == null){
            return null;
        } else {
            return userEntity.getFirstName() + " " + userEntity.getLastName();
        }
    }

    @Override
    public void addProfilePicture(String name, String imageURL) {
        UserEntity userEntity = userRepository.findByEmail(name).orElseThrow(() -> new RuntimeException("User not found!"));
        userEntity.setProfilePicture(imageURL);
        userRepository.save(userEntity);



    }

    @Override
    public String getProfilePicture(String name) {

        UserEntity userEntity = userRepository.findByEmail(name).orElse(null);
        if (userEntity == null) {
            return null;
        } else {
            return userEntity.getProfilePicture();
        }
    }

    @Override
    public String getUserId(String name) {
        return userRepository.findByEmail(name).get().getId().toString();
    }

    @Override
    public UserEntity getCreator(String name) {
        return userRepository.findByEmail(name).orElse(null);
    }

    @Override
    public List<UserViewModel> getAllNonAdminUsers(String name) {
        List<UserEntity> users = userRepository.findAllByEmailNot(name).orElse(null);
        if(users == null){
            return new ArrayList<>();
        }else{
            List<UserViewModel> userViewModels = new ArrayList<>();
            users
                    .forEach(u -> {
                        UserViewModel user = modelMapper.map(u , UserViewModel.class);
                        List<String> roles = u.getRoles().stream().map(r -> r.getName().name()).toList();
                        user.setRoles(roles);
                        userViewModels.add(user);

                    });
            return userViewModels;
        }



    }

    @Override
    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User was not found!"));
        pictureService.getAllPicturesOfUserAndDelete(userEntity.getEmail());
        userRepository.delete(userEntity);
    }

    @Override
    public void makeModerator(Long id) {
        Role role = roleService.getRoleByName(RoleEnum.MODERATOR);
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
        user.getRoles().add(role);
        userRepository.save(user);

    }

    @Override
    public void removeModeration(Long id) {

        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName(RoleEnum.USER));
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public String getAdminEmail() {
        Role adminRole = roleService.getRoleByName(RoleEnum.ADMIN);
        return userRepository.findUserEntityByRolesContains(adminRole).getEmail();

    }

    public String [] getAllAdminsAndModerators(){
        Role adminRole = roleService.getRoleByName(RoleEnum.ADMIN);
        Role moderatorRole = roleService.getRoleByName(RoleEnum.MODERATOR);

        List<UserEntity> userList = userRepository.findAllByRolesContainingOrRolesContaining(adminRole, moderatorRole);
        String [] emailList = new String[userList.size()];

        for(int i=0; i< userList.size(); i++){
            emailList[i] = userList.get(i).getEmail();
        }

        return emailList;

    }

    private void setNewAuthentication(String username, String password){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();


        PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(
                username, passwordEncoder.encode(password), authorities);

        SecurityContextHolder.getContext().setAuthentication(token);

    }


    public void updateResetPasswordToken(String token, String email) {
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Could not find a user with with an e-mail:" + email);
        }
    }

    @Override
    public void updateLastLoggedIn(String email, String latestLoggedInDate) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));
        userEntity.setLastLoggedIn(latestLoggedInDate);
        userRepository.save(userEntity);


    }

    public UserViewModel getByResetPasswordToken(String token) {
        return modelMapper.map(userRepository.findByResetPasswordToken(token), UserViewModel.class);
    }

    public void updatePassword(UserViewModel userViewModel, String newPassword) {
        UserEntity user = userRepository.findByEmail(userViewModel.getEmail()).orElse(null);
        if(user == null){
            throw new RuntimeException("User was not found!");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }


}
