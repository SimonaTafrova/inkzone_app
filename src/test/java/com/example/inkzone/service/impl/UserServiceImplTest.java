package com.example.inkzone.service.impl;

import com.example.inkzone.model.dto.binding.UserRegisterBingingModel;
import com.example.inkzone.model.entity.Role;
import com.example.inkzone.model.entity.UserEntity;
import com.example.inkzone.model.enums.RoleEnum;
import com.example.inkzone.repository.UserRepository;
import com.example.inkzone.service.EmailService;
import com.example.inkzone.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
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
    private DeleteServiceImpl pictureService;
    @Captor
    private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;


    @InjectMocks
    private UserServiceImpl toTest;

    @BeforeEach
    void setUp(){
        toTest = new UserServiceImpl(userRepository,passwordEncoder,modelMapper,roleService,emailService,pictureService);
    }

    @Test
    void testRegisterUser_firstUserGetsModeratorAndAdminRoles_registrationSuccessful(){
        when(userRepository.count()).thenReturn(0L);
        UserRegisterBingingModel userRegisterBingingModel = createUserRegisterBindingModel();

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userRegisterBingingModel.getFirstName());
        userEntity.setLastName(userRegisterBingingModel.getLastName());
        userEntity.setEmail(userRegisterBingingModel.getEmail());
        userEntity.setPassword("encoded_password");


        when(modelMapper.map(userRegisterBingingModel, UserEntity.class)).thenReturn(userEntity);

        when(passwordEncoder.encode(userRegisterBingingModel.getPassword())).thenReturn("encoded_password");


        when(roleService.getRoleByName(RoleEnum.USER)).thenReturn(createRole("USER"));
        when(roleService.getRoleByName(RoleEnum.MODERATOR)).thenReturn(createRole("MODERATOR"));
        when(roleService.getRoleByName(RoleEnum.ADMIN)).thenReturn(createRole("ADMIN"));


        toTest.registerUser(userRegisterBingingModel);

        Mockito.verify(userRepository).save(userEntityArgumentCaptor.capture());

        UserEntity actualSavedUser = userEntityArgumentCaptor.getValue();

        Assertions.assertEquals(userRegisterBingingModel.getEmail(), actualSavedUser.getEmail());
        Assertions.assertEquals("encoded_password", actualSavedUser.getPassword());
        Assertions.assertEquals("TestFirstName", actualSavedUser.getFirstName());
        Assertions.assertEquals(3, actualSavedUser.getRoles().size());

    }

    @Test
    void testRegisterUser_whenNotFirstRegisteredUser_registrationSuccessful_onlyWithUserRole(){
        when(userRepository.count()).thenReturn(1L);
        UserRegisterBingingModel userRegisterBingingModel = createUserRegisterBindingModel();

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userRegisterBingingModel.getFirstName());
        userEntity.setLastName(userRegisterBingingModel.getLastName());
        userEntity.setEmail(userRegisterBingingModel.getEmail());
        userEntity.setPassword("encoded_password");


        when(modelMapper.map(userRegisterBingingModel, UserEntity.class)).thenReturn(userEntity);

        when(passwordEncoder.encode(userRegisterBingingModel.getPassword())).thenReturn("encoded_password");


        when(roleService.getRoleByName(RoleEnum.USER)).thenReturn(createRole("USER"));


        toTest.registerUser(userRegisterBingingModel);

        Mockito.verify(userRepository).save(userEntityArgumentCaptor.capture());

        UserEntity actualSavedUser = userEntityArgumentCaptor.getValue();


        Assertions.assertEquals(1, actualSavedUser.getRoles().size());
        Assertions.assertEquals(RoleEnum.USER, actualSavedUser.getRoles().stream().findFirst().get().getName());

    }


    private UserRegisterBingingModel createUserRegisterBindingModel() {
        UserRegisterBingingModel userRegisterBingingModel = new UserRegisterBingingModel();
        userRegisterBingingModel.setEmail("test@example.com");
        userRegisterBingingModel.setFirstName("TestFirstName");
        userRegisterBingingModel.setLastName("TestLastName");
        userRegisterBingingModel.setPassword("password");
        userRegisterBingingModel.setConfirmPassword("password");

        return userRegisterBingingModel;
    }

    private Role createRole(String name) {
        Role role = new Role();
        role.setName(RoleEnum.valueOf(name));
        return role;
    }

    @Test
    void testRegisterUserThrowsWhenUserWithTheSameEmailIsRegistered(){
        UserRegisterBingingModel userRegisterBingingModel = new UserRegisterBingingModel();
        userRegisterBingingModel.setEmail("test@example.com");
        userRegisterBingingModel.setFirstName("TestFirstName");
        userRegisterBingingModel.setLastName("TestLastName");
        userRegisterBingingModel.setPassword("password");
        userRegisterBingingModel.setConfirmPassword("password");


        when(userRepository.findByEmail(userRegisterBingingModel.getEmail())).thenReturn(Optional.of(new UserEntity()));



        assertThrows(RuntimeException.class, () -> toTest.registerUser(userRegisterBingingModel));

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
                () -> toTest.registerUser(userRegisterBingingModel));

    }



}
