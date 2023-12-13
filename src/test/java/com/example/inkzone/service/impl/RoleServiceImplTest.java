package com.example.inkzone.service.impl;

import com.example.inkzone.model.entity.Role;
import com.example.inkzone.model.enums.RoleEnum;
import com.example.inkzone.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl roleService;
    @Captor
    ArgumentCaptor<Role> roleArgumentCaptor;

    @Test
    public void initRoles_whenNoRolesAreSavedInRepository_rolesAreSavedSuccessfully(){

        when(roleRepository.count()).thenReturn(0L);

        roleService.init();

        verify(roleRepository, times(3)).save(roleArgumentCaptor.capture());

        assertSame(roleArgumentCaptor.getAllValues().get(0).getName(), RoleEnum.USER);
        assertSame(roleArgumentCaptor.getAllValues().get(1).getName(), RoleEnum.MODERATOR);
        assertSame(roleArgumentCaptor.getAllValues().get(2).getName(), RoleEnum.ADMIN);

    }

    @Test
    public void initRoles_whenRolesArePresentInRepository_noNewRolesAreSaved(){

        when(roleRepository.count()).thenReturn(3L);

        roleService.init();

        verify(roleRepository, times(0)).save(roleArgumentCaptor.capture());



    }

    @Test
    public void getRoleByName_withPresentRole_ReturnsCorrectRole(){

        Role role = new Role();
        role.setName(RoleEnum.USER);


        when(roleRepository.findByName(RoleEnum.USER)).thenReturn(Optional.of(role));

        Role result = roleService.getRoleByName(RoleEnum.USER);

        assertNotNull(result);


    }



}