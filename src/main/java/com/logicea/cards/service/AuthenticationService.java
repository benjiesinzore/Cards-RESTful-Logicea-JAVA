package com.logicea.cards.service;

import com.logicea.cards.dto.LoginRequest;
import com.logicea.cards.dto.LoginResponse;
import com.logicea.cards.models.UserLogin;
import com.logicea.cards.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import com.logicea.cards.security.AuthTokenUtil;
import com.logicea.cards.security.JwtUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthTokenUtil authTokenUtil;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private final JwtUserDetailsService jwtUserDetailsService;

    public AuthenticationService(AuthTokenUtil authTokenUtil,
                                 UserRepository userRepository,
                                 AuthenticationManager authenticationManager,
                                 JwtUserDetailsService jwtUserDetailsService) {
        this.authTokenUtil = authTokenUtil;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    public LoginResponse authenticate(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            UserDetails userDetails = (User) authentication.getPrincipal();
            String token = authTokenUtil.generateToken(userDetails);
            return new LoginResponse("00", token, "success");
        } catch (AuthenticationException e) {
            return new LoginResponse("99", "Failed", "Invalid email/password");
        }
    }

    public UserLogin getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return userRepository.findByEmail(user.getUsername());
        }
        return null; // No user authenticated or user details not available
    }
}
