package com.umutbasket.berkyazici.controller;

import com.umutbasket.berkyazici.dto.AuthenticationRequest;
import com.umutbasket.berkyazici.dto.AuthenticationResponse;
import com.umutbasket.berkyazici.entity.User; // User entity'sini import et
import com.umutbasket.berkyazici.service.AuthenticationService;
import com.umutbasket.berkyazici.service.UserService; // UserService'i import et
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Wildcard import

@RestController
@RequestMapping("/api/auth") // Tüm metotlar /api/auth altında olacak
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    // YENİ METOT: Kullanıcı kaydı (Register) için
    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody User request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    // Mevcut metot: Giriş yapma (Login) için
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}