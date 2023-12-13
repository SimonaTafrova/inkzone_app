package com.example.inkzone.repository;

import com.example.inkzone.model.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture,Long> {

    Optional<List<Picture>> getAllByCreator_Email(String email);




}
