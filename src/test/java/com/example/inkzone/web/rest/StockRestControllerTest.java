package com.example.inkzone.web.rest;

import com.example.inkzone.model.dto.binding.ItemAddBindingModel;
import com.example.inkzone.model.dto.view.ItemViewModel;
import com.example.inkzone.model.entity.Item;
import com.example.inkzone.model.entity.ItemCategory;
import com.example.inkzone.model.enums.ItemCategoryEnum;
import com.example.inkzone.service.ItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static javax.management.Query.eq;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StockRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "testUsername")
    public void getAllNeedles_requestIsMade_allNeedlesAreReturned() throws Exception {
        when(itemService.getAllItems(ItemCategoryEnum.NEEDLE)).thenReturn(List.of(
                createItem("testItem1"),
                createItem("testItem2")
        ));

        mockMvc.perform(get("/api/stock/needles").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("testItem1")))
                .andExpect(jsonPath("$.[1].name", is("testItem2")));

    }

    @Test
    @WithMockUser(username = "testUsername")
    public void getAllOthers_requestIsMade_allOthersAreReturned() throws Exception {
        when(itemService.getAllItems(ItemCategoryEnum.OTHER)).thenReturn(List.of(
                createItem("testItem1"),
                createItem("testItem2")
        ));

        mockMvc.perform(get("/api/stock/others").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("testItem1")))
                .andExpect(jsonPath("$.[1].name", is("testItem2")));

    }

    @Test
    @WithMockUser(username = "testUsername")
    public void getAllInks_requestIsMade_allInksAreReturned() throws Exception {
        when(itemService.getAllItems(ItemCategoryEnum.INK)).thenReturn(List.of(
                createItem("testItem1"),
                createItem("testItem2")
        ));

        mockMvc.perform(get("/api/stock/inks").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("testItem1")))
                .andExpect(jsonPath("$.[1].name", is("testItem2")));

    }

    @Test
    public void createItem_withAnonymousUser_forbiddenStatusIsReturned() throws Exception {
        ItemAddBindingModel itemAddBindingModel = new ItemAddBindingModel();
        itemAddBindingModel.setName("testItem");
        itemAddBindingModel.setQuantity(20);
        itemAddBindingModel.setMinQuantity(10);
        itemAddBindingModel.setCategory("NEEDLE");

        mockMvc.perform(post("/api/stock/")
                .content(objectMapper.writeValueAsString(itemAddBindingModel)))
                .andExpect(status().isForbidden());

    }




    @Test
    @WithMockUser(username = "testUser")
    public void createItem_withUser_ItemIsCreated() throws Exception {
        ItemAddBindingModel itemAddBindingModel = new ItemAddBindingModel();
        itemAddBindingModel.setName("testItem");
        itemAddBindingModel.setQuantity(20);
        itemAddBindingModel.setMinQuantity(10);
        itemAddBindingModel.setCategory("NEEDLE");

        when(itemService.addItem(itemAddBindingModel)).thenReturn(1L);

        mockMvc.perform(post("/api/stock/")
                        .content(objectMapper.writeValueAsString(itemAddBindingModel))
                        .with(csrf())
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isCreated());

    }


    @Test
    public void deleteItem_anonymousUser_forbiddenStatusIsReturned() throws Exception {
        mockMvc.perform(delete("/api/stock/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void deleteItem_withAuthenticatedUser_itemDeletedSuccessfully() throws Exception {
        when(itemService.deleteItem(1L)).thenReturn(createItem("testItem"));

        mockMvc.perform(delete("/api/stock/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("testItem")))
                .andExpect(jsonPath("$.quantity",is(20)));


    }

    @Test
    @WithMockUser(username = "testUser")
    public void editItem_withAuthenticatedUser_itemEditedSuccessfully() throws Exception {
        ItemAddBindingModel itemAddBindingModel = new ItemAddBindingModel();
        itemAddBindingModel.setName("testName");
        itemAddBindingModel.setQuantity(20);
        itemAddBindingModel.setMinQuantity(10);
        itemAddBindingModel.setCategory("NEEDLE");



        when(itemService.editItem(ArgumentMatchers.eq((1L)), any())).thenReturn(createItem("testNameResult"));

        mockMvc.perform(put("/api/stock/update/1")
                .content(objectMapper.writeValueAsString(itemAddBindingModel))
                .with(csrf())
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("testNameResult")));

    }


    @Test
    @WithMockUser(username = "testUser")
    public void editItem_whenIdIsNotPresent_ThrowsNotFound() throws Exception {
        ItemAddBindingModel itemAddBindingModel = new ItemAddBindingModel();
        itemAddBindingModel.setName("testName");
        itemAddBindingModel.setQuantity(20);
        itemAddBindingModel.setMinQuantity(10);
        itemAddBindingModel.setCategory("NEEDLE");

        when(itemService.editItem(ArgumentMatchers.eq(1L), any())).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/stock/update/1")
                        .content(objectMapper.writeValueAsString(itemAddBindingModel))
                        .with(csrf())
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isNotFound());



    }






    public ItemViewModel createItem(String name){
        ItemViewModel item = new ItemViewModel();


        item.setName(name);
        item.setQuantity(20);
        item.setMinQuantity(10);


        return item;

    }
}
