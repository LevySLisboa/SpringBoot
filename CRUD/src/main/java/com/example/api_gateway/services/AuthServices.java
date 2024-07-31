package com.example.api_gateway.services;

import com.example.api_gateway.data.vo.v1.security.AccountCredentialsVO;
import com.example.api_gateway.data.vo.v1.security.TokenVO;
import com.example.api_gateway.repositories.UserRepository;
import com.example.api_gateway.security.jwt.JwtTokenProvider;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthServices {
    @Autowired
    private JwtTokenProvider provider;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserRepository repository;

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserServices.class);
    private Logger logger = Logger.getLogger(UserServices.class.getName());

    public ResponseEntity singin(AccountCredentialsVO data) {
        try {
            var userName = data.getUserName();
            var password = data.getPassword();
            manager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
            var user = repository.findByUsername(userName);
            var tokenResponse = new TokenVO();
            if (user != null) {
                tokenResponse = provider.createAccessToken(userName, user.getRoles());
            } else {
                throw new UsernameNotFoundException(STR."Username \{userName} not found!");
            }
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }


}
