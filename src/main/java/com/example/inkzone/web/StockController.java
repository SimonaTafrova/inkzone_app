package com.example.inkzone.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stock")
public class StockController {

    @GetMapping("/needles")
    public String getStock(){
        return "needles";
    }

    @GetMapping("/inks")
    public String getInks(){
        return "inks";
    }

    @GetMapping("/others")
    public String getOthers(){
        return "otherItems";
    }
}
