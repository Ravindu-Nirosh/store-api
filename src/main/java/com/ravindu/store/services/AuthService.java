package com.ravindu.store.services;

import com.ravindu.store.entities.User;
import com.ravindu.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User getCurrentUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId =(Long) authentication.getPrincipal();
        return userRepository.findById(userId).orElse(null);
    }
}
