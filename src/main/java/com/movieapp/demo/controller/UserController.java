package com.movieapp.demo.controller;

import com.movieapp.demo.services.UserDetailService;
import com.movieapp.demo.jwt.JwtProperties;
import com.movieapp.demo.jwt.JwtResponse;
import com.movieapp.demo.models.LoginModel;
import com.movieapp.demo.models.UserEntity;
import com.movieapp.demo.repository.UserRepository;
import com.movieapp.demo.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginModel loginModel) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginModel.getUsername(), loginModel.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = authService.generateJwtToken(authentication);

        UserDetailService userDetails = (UserDetailService) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername()));

    }

    @GetMapping("/getUsers")
    public Object getUsers(){
        try{
            List<UserEntity> users =  this.userRepository.findAll();

            return users;
        } catch (Exception exception){
            return exception;
        }
    }
    @PostMapping("/register")
    public Object insertUser(@RequestBody UserEntity userEntity )
    {
        try {
            String password = passwordEncoder.encode(userEntity.getPassword());
            userEntity.setPassword(password);
            return this.userRepository.save(userEntity);
        } catch(Exception exception)
        {
            return exception;
        }
    }
}
