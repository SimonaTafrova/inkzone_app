package com.example.inkzone.model.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity{
    @Column(nullable = false)
    private String url;
    @ManyToOne
    private UserEntity creator;
    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @Column
    private Set<String> materialsUsed;

    public Picture() {
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<String> getMaterialsUsed() {
        return materialsUsed;
    }

    public void setMaterialsUsed(Set<String> materialsUsed) {
        this.materialsUsed = materialsUsed;
    }
}
