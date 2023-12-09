package com.example.inkzone.model.dto.view;

import com.example.inkzone.model.entity.Picture;

import java.util.List;
import java.util.Set;

public class PictureGalleryViewModel {
    private Long id;
    private String url;
    private String description;

    private List<String> materialsUsed;

    public PictureGalleryViewModel() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMaterialsUsed() {
        return materialsUsed;
    }

    public void setMaterialsUsed(List<String> materialsUsed) {
        this.materialsUsed = materialsUsed;
    }
}
