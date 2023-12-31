package com.example.inkzone.model.dto.binding;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRegisterBingingModel {
    @NotNull
    @Size(min = 3, max = 20)
    private String firstName;
    @NotNull
    @Size(min = 3, max = 50)
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Size(min = 5, max = 20)
    private String password;
    @NotNull
    @Size(min = 5, max = 20)
    private String confirmPassword;

    public UserRegisterBingingModel() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
