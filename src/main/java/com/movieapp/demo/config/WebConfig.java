package com.movieapp.demo.config;

import com.movieapp.demo.jwt.AuthenticationFilter;
import com.movieapp.demo.jwt.AuthorizationFilter;
import com.movieapp.demo.repository.UserRepository;
import com.movieapp.demo.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity // Entry point into spring security
public class WebConfig extends WebSecurityConfigurerAdapter {
    private UserRepository userRepository;
    private UserService userService;

    public WebConfig(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Is against cross side request. Which prevents attacker from forcing users to execute code in their browsers
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless if user closes user session, all his data is deleted
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager()))
                .addFilter(new AuthorizationFilter(authenticationManager(), this.userService, this.userRepository))
                .authorizeRequests() // We decide which endpoints are public, private and on-authenticate
               // .antMatchers(HttpMethod.POST, "/login").permitAll() // we didn't define the /login endpoint, SpringBoot did
                .antMatchers("/api/user/**").permitAll() // is suffixed by permitAll()
                .anyRequest().authenticated(); //For any other request user should authenticate first !Done
    }

    /**
     *  This annotation means this function can be accessed anywhere within your package
     *  This method initialize the userService for the authenticationProvider which is provided by SpringBoot
     * @return
     */
    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userService);
        return daoAuthenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
