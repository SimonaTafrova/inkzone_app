package com.example.inkzone.web.rest;

import com.example.inkzone.exceptionHandling.ErrorApiResponse;
import com.example.inkzone.model.dto.binding.ItemAddBindingModel;
import com.example.inkzone.model.dto.view.ItemViewModel;
import com.example.inkzone.model.enums.ItemCategoryEnum;
import com.example.inkzone.service.ItemService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock")
public class StockRestController {
    private final ItemService itemService;

    public StockRestController(ItemService itemService) {
        this.itemService = itemService;
    }



    @GetMapping("/needles")
    public ResponseEntity<List<ItemViewModel>> getNeedles(){

        return ResponseEntity.ok(itemService.getAllItems(ItemCategoryEnum.NEEDLE));
    }

    @GetMapping("/inks")
    public ResponseEntity<List<ItemViewModel>> getInks(){
        return ResponseEntity.ok(itemService.getAllItems(ItemCategoryEnum.INK));
    }

    @GetMapping("/others")
    public ResponseEntity<List<ItemViewModel>> getOthers(){
        return ResponseEntity.ok(itemService.getAllItems(ItemCategoryEnum.OTHER));
    }

    @PostMapping()
    public ResponseEntity<ItemViewModel> addItem(@Valid @RequestBody ItemAddBindingModel itemAddBindingModel,
                                                 BindingResult bindingResult,
                                                 UriComponentsBuilder uriComponentsBuilder){

        long newItemId = itemService.addItem(itemAddBindingModel);
        ItemViewModel itemViewModel = itemService.getItemViewModel(newItemId);

        URI locationOfNewItem =
                URI.create(String.format("/api/stock/%s", newItemId));

        return ResponseEntity.
                created(locationOfNewItem).
                body(itemViewModel);


    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ItemViewModel> editItem(@PathVariable("id") Long id, @Valid @RequestBody ItemAddBindingModel itemAddBindingModel){
        try {
            ItemViewModel itemViewModel = itemService.editItem(id, itemAddBindingModel);
            return ResponseEntity.ok(itemViewModel);
        }catch (RuntimeException ex){
            return ResponseEntity.notFound().build();
        }

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ItemViewModel> deleteItem(@PathVariable("id") Long id){
        ItemViewModel itemViewModel = itemService.deleteItem(id);
        return  ResponseEntity.ok(itemViewModel);

    }












}

