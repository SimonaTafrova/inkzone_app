package com.example.inkzone.repository;

import com.example.inkzone.model.entity.Role;
import com.example.inkzone.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(RoleEnum roleEnum);


}
