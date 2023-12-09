package com.example.inkzone.init;

import com.example.inkzone.service.ItemCategoryService;
import com.example.inkzone.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataBaseInit implements CommandLineRunner {
    private final RoleService roleService;
    private final ItemCategoryService itemCategoryService;

    public DataBaseInit(RoleService roleService, ItemCategoryService itemCategoryService) {
        this.roleService = roleService;
        this.itemCategoryService = itemCategoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        roleService.init();
        itemCategoryService.init();

    }
}
