package com.example.inkzone.web;

import com.example.inkzone.model.dto.view.UserViewModel;
import com.example.inkzone.service.UserService;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.security.Principal;

@Controller
@EnableWebSecurity
@RequestMapping("/users/edit")
public class UserEditController {
    private final UserService userService;

    public UserEditController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public String getEditPage(Principal principal, Model model){
        UserViewModel userViewModel = userService.getUserByUserName(principal.getName());
        model.addAttribute("userFullName", userViewModel.getFirstName() + " " + userViewModel.getLastName());
        model.addAttribute("userViewModel", userViewModel);
        return "edit";
    }


    @PostMapping("/firstName")
    public String changeFirstName(@RequestParam("newFirstName") String firstName,
                                  @RequestParam("firstNameConfirmPassword") String password,
                                  Principal principal){

        userService.editFirstName(principal.getName(), firstName, password);


        return "redirect:/users/edit";

    }

    @PostMapping("/lastName")
    public String changeLastName(@RequestParam("newLastName") String lastName,
                                  @RequestParam("lastNameConfirmPassword") String password,
                                  Principal principal){

        userService.editLastName(principal.getName(), lastName, password);


        return "redirect:/users/edit";

    }

    @PostMapping("/email")
    public String changeEmail(@RequestParam("newEmail") String newEmail,
                                 @RequestParam("emailConfirmPassword") String password,
                                 Principal principal){


        userService.editEmail(principal.getName(),newEmail,password);



        return "redirect:/users/edit";

    }

    @PostMapping("/password")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                              @RequestParam("newPasswordConfirm") String newPasswordConfirm,
                              @RequestParam("OldPasswordConfirm") String oldPasswordConfirm,
                                 Principal principal) {


        userService.editPassword(newPassword, newPasswordConfirm, oldPasswordConfirm, principal.getName());



        return "redirect:/users/edit";

    }





}
