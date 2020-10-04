package com.movieapp.demo.models;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
public class LoginModel {
    private String username;
    private String password;

    private String token;

    public LoginModel(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
