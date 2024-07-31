package com.example.api_gateway.services;

import com.example.api_gateway.repositories.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserServices implements UserDetailsService {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserServices.class);
    private Logger logger = Logger.getLogger(UserServices.class.getName());

    @Autowired
    private UserRepository repository;

    public UserServices(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info(STR."Find one User by name \{username}!");
        var user = repository.findByUsername(username);
        if (user!=null){
            return user;
        }else{
            throw new UsernameNotFoundException(STR."Username \{username} not found!");
        }
    }
}
