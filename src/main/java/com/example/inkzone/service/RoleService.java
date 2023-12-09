package com.example.inkzone.service;

import com.example.inkzone.model.entity.Role;
import com.example.inkzone.model.enums.RoleEnum;

public interface RoleService {
    void init();


    Role getRoleByName(RoleEnum roleEnum);
}
