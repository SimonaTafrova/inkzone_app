package com.example.inkzone.web;

import com.example.inkzone.model.dto.binding.UserRegisterBingingModel;
import com.example.inkzone.model.dto.view.UserViewModel;
import com.example.inkzone.model.entity.UserEntity;
import com.example.inkzone.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@SpringBootTest
@AutoConfigureMockMvc
class UserRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private UserEntity user = new UserEntity();

    @BeforeEach
    void createUser(){
        Random random = new Random();
        String emailPrefix = String.valueOf(random.nextInt(10000));
        this.user.setFirstName("Pesho");
        this.user.setLastName("Peshev");

        this.user.setEmail(emailPrefix + "@test.com");
        this.user.setPassword("123456");

    }


    @Test
    void testOpenRegisterForm() throws Exception {
        mockMvc.
                perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testRegisterUser() throws Exception {

        Long usersCount = userRepository.count();


        mockMvc.perform(post("/users/register").
                        param("firstName", user.getFirstName()).
                        param("lastName", user.getLastName()).
                        param("email", user.getEmail()).
                        param("password", user.getPassword()).
                        param("confirmPassword", user.getPassword()).
                        with(csrf()).
                        contentType(MediaType.APPLICATION_FORM_URLENCODED)
                ).
                andExpect(status().is3xxRedirection());

        Assertions.assertEquals(usersCount + 1, userRepository.count());






    }
}
