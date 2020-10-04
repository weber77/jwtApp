package com.movieapp.demo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.movieapp.demo.repository.UserRepository;
import com.movieapp.demo.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
// Used to check user authorities and roles and authenticate
public class AuthorizationFilter extends BasicAuthenticationFilter {
    private UserService userService;
    private UserRepository userRepository;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UserService userService, UserRepository userRepository) {
        super(authenticationManager);
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     *
     * @param request contains header and user token
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
             String header = request.getHeader(JwtProperties.HEADER_STRING); // get the header authentication type (application/json)
             if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)){ //if header doesn't start with BEARER , return
                 chain.doFilter(request, response);
                 return;
             }

             Authentication authentication = authentication(request);
             SecurityContextHolder.getContext().setAuthentication(authentication);
             chain.doFilter(request, response); // verifies your request comply to JWT standards
    }

    /**
     * Decrypts the JWT into username and secret-key.
     * After which it tries to authenticate the user from the DB using the UserService
     * @param request contains username and secret-key
     * @return
     */
    private Authentication authentication(HttpServletRequest request){

        String token = request.getHeader(JwtProperties.HEADER_STRING) // Header: Bearer sidnflksdbqglkshbvsdqqsblqusvlidsngds
                .replace(JwtProperties.TOKEN_PREFIX, "");
        if(token != null){

            String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes())) // Verify and decode token
                    .build()
                    .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""))
                    .getSubject();

            if(username != null){
                UserDetails userDetails = this.userService.loadUserByUsername(username);
                return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

            }
            return null;
        }
        return null;
    }

}
