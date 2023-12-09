package com.example.inkzone.service.impl;

import com.example.inkzone.model.entity.Role;
import com.example.inkzone.model.enums.RoleEnum;
import com.example.inkzone.repository.RoleRepository;
import com.example.inkzone.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void init() {
        if (roleRepository.count() == 0) {
            Arrays.stream(RoleEnum.values()).forEach(roleEnum -> {
                Role role = new Role();
                role.setName(roleEnum);
                roleRepository.save(role);
            });

        }
    }

    @Override
    public Role getRoleByName(RoleEnum roleEnum) {
        return roleRepository.findByName(roleEnum).orElse(null);
    }
}
