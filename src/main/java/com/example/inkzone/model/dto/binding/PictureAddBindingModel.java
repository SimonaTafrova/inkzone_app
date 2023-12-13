package com.example.inkzone.model.dto.binding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class PictureAddBindingModel {


    private String url;
    @NotNull
    @NotBlank
    private String description;

    private List<String> materialsUsed;

    public PictureAddBindingModel() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
