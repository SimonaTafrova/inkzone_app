package com.example.inkzone.repository;

import com.example.inkzone.model.entity.Role;
import com.example.inkzone.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<List<UserEntity>> findAllByEmailNot(String email);

    UserEntity findUserEntityByRolesContains(Role role);


    List<UserEntity> findAllByRolesContainingOrRolesContaining(Role role, Role role2);


    UserEntity findByResetPasswordToken(String resetPasswordToken);





}
