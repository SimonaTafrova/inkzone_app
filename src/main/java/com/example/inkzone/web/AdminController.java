package com.example.inkzone.web;

import com.example.inkzone.model.dto.view.UserViewModel;
import com.example.inkzone.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAdminPage(Model model, Principal principal){
        List<UserViewModel> allUsers = userService.getAllNonAdminUsers(principal.getName());
        if(allUsers.isEmpty()){
            model.addAttribute("noUsers", "There are no registered users yet!");
        }
            model.addAttribute("users", allUsers);


        return "adminPage";
    }

    @PatchMapping("/moderator/{id}")
    public String makeModerator(@PathVariable("id") Long id){
        userService.makeModerator(id);
        return "redirect:/admin";
    }

    @PatchMapping("/removemoderator/{id}")
    public String removeModeration(@PathVariable("id") Long id){
        userService.removeModeration(id);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
