package com.movieapp.demo.services;



import com.movieapp.demo.models.UserEntity;
import com.movieapp.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    private UserEntity userEntity;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        userEntity = this.userRepository.findByUsername(s); // search for the username and return user if found

        return new UserDetailService(userEntity);
    }
}
