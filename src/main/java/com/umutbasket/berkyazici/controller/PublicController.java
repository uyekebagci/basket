package com.umutbasket.berkyazici.controller;

import com.umutbasket.berkyazici.dto.AuthenticationRequest;
import com.umutbasket.berkyazici.dto.AuthenticationResponse;
import com.umutbasket.berkyazici.dto.RegisterRequestDTO;
import com.umutbasket.berkyazici.dto.UserResponseDTO;
import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.service.security.AuthenticationService;
import com.umutbasket.berkyazici.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public") // Bu controller'daki tüm yollar /api/public ile başlayacak
@RequiredArgsConstructor
public class PublicController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    // "Kayıt Ol" (Register) Endpoint
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        User createdUser = userService.createUser(request);

        UserResponseDTO responseDto = UserResponseDTO.builder()
                .userId(createdUser.getUserId())
                .firstName(createdUser.getFirstName())
                .lastName(createdUser.getLastName())
                .email(createdUser.getEmail())
                .birthDay(createdUser.getBirthDay())
                .height(createdUser.getHeight())
                .weight(createdUser.getWeight())
                .age(createdUser.getAge())
                .gender(createdUser.getGender())
                .role(createdUser.getRole())
                .plan(createdUser.getPlan())
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // "Giriş Yap" (Login) Endpoint
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}