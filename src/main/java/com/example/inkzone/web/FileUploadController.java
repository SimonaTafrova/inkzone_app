package com.example.inkzone.web;

import com.example.inkzone.service.FileUploadService;
import com.example.inkzone.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
public class FileUploadController {
    private final FileUploadService fileUploadService;
    private final UserService userService;

    public FileUploadController(FileUploadService fileUploadService, UserService userService) {
        this.fileUploadService = fileUploadService;
        this.userService = userService;
    }

    @PostMapping("/users/picture/upload")
    public String uploadProfilePicture(@RequestParam("image")MultipartFile multipartFile,
                                       Model model, Principal principal) throws IOException {
        String imageURL = fileUploadService.uploadFile(multipartFile);
        model.addAttribute("image", imageURL);
        userService.addProfilePicture(principal.getName(), imageURL);
        return "redirect:/users/edit";
    }
}
