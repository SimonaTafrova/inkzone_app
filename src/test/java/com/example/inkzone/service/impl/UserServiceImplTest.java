package com.example.inkzone.service.impl;

import com.example.inkzone.model.dto.binding.UserRegisterBingingModel;
import com.example.inkzone.model.entity.Role;
import com.example.inkzone.model.entity.UserEntity;
import com.example.inkzone.model.enums.RoleEnum;
import com.example.inkzone.repository.UserRepository;
import com.example.inkzone.service.EmailService;
import com.example.inkzone.service.RoleService;
import com.example.inkzone.service.UserService;
import com.example.inkzone.service.impl.PictureDeleteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private  UserRepository userRepository;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RoleService roleService;
    @Mock
    private EmailService emailService;
    @Mock
    private  PictureDeleteService pictureService;
    @Captor
    private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;


    private UserServiceImpl toTest;

    @BeforeEach
    void setUp(){
        toTest = new UserServiceImpl(userRepository,passwordEncoder,modelMapper,roleService,emailService,pictureService);
    }

    @Test
    void testRegisterUser(){
        UserRegisterBingingModel userRegisterBingingModel = new UserRegisterBingingModel();
        userRegisterBingingModel.setEmail("test@example.com");
        userRegisterBingingModel.setFirstName("TestFirstName");
        userRegisterBingingModel.setLastName("TestLastName");
        userRegisterBingingModel.setPassword("password");
        userRegisterBingingModel.setConfirmPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userRegisterBingingModel.getFirstName());
        userEntity.setLastName(userRegisterBingingModel.getLastName());
        userEntity.setEmail(userRegisterBingingModel.getEmail());
        userEntity.setPassword("encoded_password");

        Role testUserRole = new Role();
        testUserRole.setName(RoleEnum.USER);
        Role testModeratorRole = new Role();
        testModeratorRole.setName(RoleEnum.MODERATOR);
        Role testAdminRole = new Role();
        testAdminRole.setName(RoleEnum.ADMIN);

        userEntity.setRoles(Set.of(testUserRole, testModeratorRole, testAdminRole));

        when(modelMapper.map(userRegisterBingingModel, UserEntity.class)).thenReturn(userEntity);

        when(passwordEncoder.encode(userRegisterBingingModel.getPassword())).thenReturn("encoded_password");


        toTest.registerUser(userRegisterBingingModel);

        Mockito.verify(userRepository).save(userEntityArgumentCaptor.capture());

        UserEntity actualSavedUser = userEntityArgumentCaptor.getValue();

        Assertions.assertEquals(userRegisterBingingModel.getEmail(), actualSavedUser.getEmail());
        Assertions.assertEquals("encoded_password", actualSavedUser.getPassword());
        Assertions.assertEquals("TestFirstName", actualSavedUser.getFirstName());

    }

    @Test
    void testRegisterUserThrowsWhenUserWithTheSameEmailIsRegistered(){
        UserRegisterBingingModel userRegisterBingingModel = new UserRegisterBingingModel();
        userRegisterBingingModel.setEmail("test@example.com");
        userRegisterBingingModel.setFirstName("TestFirstName");
        userRegisterBingingModel.setLastName("TestLastName");
        userRegisterBingingModel.setPassword("password");
        userRegisterBingingModel.setConfirmPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userRegisterBingingModel.getFirstName());
        userEntity.setLastName(userRegisterBingingModel.getLastName());
        userEntity.setEmail(userRegisterBingingModel.getEmail());
        userEntity.setPassword("encoded_password");

        Role testUserRole = new Role();
        testUserRole.setName(RoleEnum.USER);
        Role testModeratorRole = new Role();
        testModeratorRole.setName(RoleEnum.MODERATOR);
        Role testAdminRole = new Role();
        testAdminRole.setName(RoleEnum.ADMIN);

        userEntity.setRoles(Set.of(testUserRole, testModeratorRole, testAdminRole));

        when(modelMapper.map(userRegisterBingingModel, UserEntity.class)).thenReturn(userEntity);

        when(passwordEncoder.encode(userRegisterBingingModel.getPassword())).thenReturn("encoded_password");

        UserRegisterBingingModel userRegisterBingingModelSecond = new UserRegisterBingingModel();
        userRegisterBingingModelSecond.setEmail("test@example.com");
        userRegisterBingingModelSecond.setFirstName("TestFirstName");
        userRegisterBingingModelSecond.setLastName("TestLastName");
        userRegisterBingingModelSecond.setPassword("password");
        userRegisterBingingModelSecond.setConfirmPassword("password");

        UserEntity userEntitySecond = new UserEntity();
        userEntitySecond.setFirstName(userRegisterBingingModel.getFirstName());
        userEntitySecond.setLastName(userRegisterBingingModel.getLastName());
        userEntitySecond.setEmail(userRegisterBingingModel.getEmail());
        userEntitySecond.setPassword("encoded_password");

        userEntity.setRoles(Set.of(testUserRole));

        when(modelMapper.map(userRegisterBingingModelSecond, UserEntity.class)).thenReturn(userEntitySecond);

        when(passwordEncoder.encode(userRegisterBingingModel.getPassword())).thenReturn("encoded_password");

        toTest.registerUser(userRegisterBingingModel);
        Mockito.verify(userRepository).save(userEntityArgumentCaptor.capture());

        assertThrows(RuntimeException.class, () -> toTest.registerUser(userRegisterBingingModelSecond));







    }


    @Test
    void testRegisterUserFailsWhenPasswordsDontMatch(){
        UserRegisterBingingModel userRegisterBingingModel = new UserRegisterBingingModel();
        userRegisterBingingModel.setEmail("test@example.com");
        userRegisterBingingModel.setFirstName("TestFirstName");
        userRegisterBingingModel.setLastName("TestLastName");
        userRegisterBingingModel.setPassword("password12");
        userRegisterBingingModel.setConfirmPassword("password");

        assertThrows(RuntimeException.class,
                () -> {
                    toTest.registerUser(userRegisterBingingModel);

                });

    }
}
