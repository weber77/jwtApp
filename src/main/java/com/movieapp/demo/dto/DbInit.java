package com.movieapp.demo.dto;


import com.movieapp.demo.models.UserEntity;
import com.movieapp.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DbInit implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder){

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args){
//Delete all
        this.userRepository.deleteAll();


//create users
        UserEntity dan = new UserEntity("Dan", passwordEncoder.encode("Dan123"));
        UserEntity dan2 = new UserEntity( "Dan2", passwordEncoder.encode("Dan2123"));
        UserEntity dan3 = new UserEntity( "Dan3", passwordEncoder.encode("Dan3123"));
        UserEntity dan4 = new UserEntity( "Dan4", passwordEncoder.encode("Dan4123"));



        List<UserEntity> users = Arrays.asList(dan, dan2, dan3, dan4);


//Save to db
        this.userRepository.saveAll(users);

    }
}

