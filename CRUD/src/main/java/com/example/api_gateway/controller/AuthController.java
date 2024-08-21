package com.example.api_gateway.controller;

import com.example.api_gateway.data.vo.v1.security.AccountCredentialsVO;
import com.example.api_gateway.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication endpoints")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServices services;

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Authenticates a user and returns a token")
    @PostMapping(value = "/signin")
    public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {
        if (checkIfParamsIsNotNull(data)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        var token = services.singin(data);

        if(token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        else return token;
    }

    @Operation(summary = "Refresh a token for authenticated user and returns a token")
    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity refreshToken(@PathVariable(value = "username") String userName,@RequestHeader("Authorization")String refreshToken) {
        if (checkIfParamsIsNotNull(userName, refreshToken)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        var token = services.refreshToken(userName,refreshToken);

        if(token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        else return token;
    }

    private static boolean checkIfParamsIsNotNull(String userName, String refreshToken) {
        return userName.isBlank() || refreshToken.isBlank();
    }

    private static boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
        return data == null || data.getUserName() == null || data.getPassword().isBlank() ||
                data.getUserName().isBlank() || data.getPassword() == null;
    }
}
