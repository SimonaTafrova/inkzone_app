package com.example.inkzone.web;

import com.example.inkzone.model.dto.binding.PictureAddBindingModel;
import com.example.inkzone.model.dto.view.ItemViewModel;
import com.example.inkzone.model.dto.view.PictureGalleryViewModel;
import com.example.inkzone.model.enums.ItemCategoryEnum;
import com.example.inkzone.service.FileUploadService;
import com.example.inkzone.service.ItemService;
import com.example.inkzone.service.PictureService;
import com.example.inkzone.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/gallery")
public class GalleryController {
    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";
    private final UserService userService;
    private final PictureService pictureService;

    private final FileUploadService fileUploadService;


    public GalleryController(UserService userService, PictureService pictureService, FileUploadService fileUploadService) {
        this.userService = userService;
        this.pictureService = pictureService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping
    public String getGallery(Model model, Principal principal){
        String id = userService.getUserId(principal.getName());
        model.addAttribute("userId", id);
        List<ItemViewModel> items = pictureService.getAllItems(ItemCategoryEnum.NEEDLE);
        model.addAttribute("items", items);
        List<PictureGalleryViewModel> pictures = pictureService.getAllPicturesByUserId(principal.getName());
        model.addAttribute("pictures", pictures);

        return "gallery";
    }

    @GetMapping("/{id}")
    private String getPicture(@PathVariable("id") Long id, Model model){
        PictureGalleryViewModel pictureGalleryViewModel = pictureService.getPictureById(id);
        model.addAttribute("pictureGalleryViewModel", pictureGalleryViewModel);
        return "imagePage";
    }

    @PostMapping("/add")
    public String postPicture(@RequestParam("image") MultipartFile multipartFile,
                              @Valid PictureAddBindingModel pictureAddBindingModel,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest request,
                              Principal principal) throws IOException {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("pictureAddBindingModel", pictureAddBindingModel);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + "pictureAddBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("isInvalid", "There was a problem uploading your picture!Please try again!");

            return "redirect:/gallery";
        }

        String[] selected_nodes = request.getParameterValues("selected_nodes");
        String imageURL = fileUploadService.uploadFile(multipartFile);
        pictureAddBindingModel.setUrl(imageURL);
        pictureAddBindingModel.setMaterialsUsed(Arrays.stream(selected_nodes).toList());
        pictureService.savePicture(pictureAddBindingModel,principal.getName());




        return "redirect:/gallery";
    }

    @DeleteMapping("{id}/delete")
    public String deletePicture(@PathVariable("id") Long id) throws IOException {
        PictureGalleryViewModel pictureGalleryViewModel = pictureService.deletePictureById(id);
        fileUploadService.delete(pictureGalleryViewModel.getUrl());

        return "redirect:/gallery";
    }

    @ModelAttribute
    public PictureAddBindingModel pictureAddBindingModel(){
        return new PictureAddBindingModel();
    }

}
