package com.example.inkzone.web;

import com.example.inkzone.model.dto.view.UserViewModel;
import com.example.inkzone.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/calendar")
public class CalendarController {
    private final UserService userService;

    public CalendarController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getCalendar(Principal principal, Model model){
        model.addAttribute("loggedUser", userService.getUserByUserName(principal.getName()).getFirstName());
        model.addAttribute("userId", userService.getUserId(principal.getName()));

        return "calendar";
    }





}
