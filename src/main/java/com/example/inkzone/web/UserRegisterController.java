package com.example.inkzone.web;

import com.example.inkzone.model.dto.binding.UserRegisterBingingModel;
import com.example.inkzone.service.UserService;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/users")
public class UserRegisterController {
    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";
    private final UserService userService;


    public UserRegisterController(UserService userService) {
        this.userService = userService;

    }


    @ModelAttribute("userRegisterBindingModel")
    public UserRegisterBingingModel userRegisterBingingModel(){
        return new UserRegisterBingingModel();
    }



    @GetMapping("/register")
    public String getRegister(){
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid UserRegisterBingingModel userRegisterBingingModel, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors() || !userRegisterBingingModel.getPassword().equals(userRegisterBingingModel.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBingingModel);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + "userRegisterBindingModel", bindingResult);
             if(!userRegisterBingingModel.getPassword().equals(userRegisterBingingModel.getConfirmPassword())){
                 redirectAttributes.addFlashAttribute("mismatch", true);
            }


            return "redirect:/users/register";
        }

        userService.registerUser(userRegisterBingingModel);

        return "redirect:/users/login";

    }




}
