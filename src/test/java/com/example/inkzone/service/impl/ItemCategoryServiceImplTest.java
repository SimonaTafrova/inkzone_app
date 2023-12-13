package com.example.inkzone.service.impl;

import com.example.inkzone.model.entity.ItemCategory;
import com.example.inkzone.model.entity.Role;
import com.example.inkzone.model.enums.ItemCategoryEnum;
import com.example.inkzone.model.enums.RoleEnum;
import com.example.inkzone.repository.ItemCategoryRepository;
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
class ItemCategoryServiceImplTest {

    @Mock
    private ItemCategoryRepository itemCategoryRepository;
    @InjectMocks
    private ItemCategoryServiceImpl itemCategoryService;
    @Captor
    ArgumentCaptor<ItemCategory> itemCategoryArgumentCaptor;

    @Test
    public void initRoles_whenNoRolesAreSavedInRepository_rolesAreSavedSuccessfully(){

        when(itemCategoryRepository.count()).thenReturn(0L);

        itemCategoryService.init();

        verify(itemCategoryRepository, times(3)).save(itemCategoryArgumentCaptor.capture());

        assertSame(itemCategoryArgumentCaptor.getAllValues().get(0).getName(), ItemCategoryEnum.NEEDLE);
        assertSame(itemCategoryArgumentCaptor.getAllValues().get(1).getName(), ItemCategoryEnum.INK);
        assertSame(itemCategoryArgumentCaptor.getAllValues().get(2).getName(), ItemCategoryEnum.OTHER);

    }

    @Test
    public void initRoles_whenRolesArePresentInRepository_noNewRolesAreSaved(){

        when(itemCategoryRepository.count()).thenReturn(3L);

        itemCategoryService.init();

        verify(itemCategoryRepository, times(0)).save(itemCategoryArgumentCaptor.capture());



    }

    @Test
    public void getRoleByName_withPresentRole_ReturnsCorrectRole(){

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setName(ItemCategoryEnum.NEEDLE);


        when(itemCategoryRepository.getItemCategoryByName(ItemCategoryEnum.NEEDLE)).thenReturn(Optional.of(itemCategory));

        ItemCategory result = itemCategoryService.getCategory(ItemCategoryEnum.NEEDLE);

        assertNotNull(result);


    }



}