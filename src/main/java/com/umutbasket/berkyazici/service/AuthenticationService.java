package com.umutbasket.berkyazici.service;

import com.umutbasket.berkyazici.dto.AuthenticationRequest;
import com.umutbasket.berkyazici.dto.AuthenticationResponse;
import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    // YAPIYI BASİTLEŞTİRİYORUZ: Doğrudan AuthenticationManager'ı enjekte et
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        // Kimlik doğrulama işlemini yap
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Kimlik doğrulama başarılıysa, kullanıcıyı bul ve token üret
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Kullanıcı doğrulandı ama veritabanında bulunamadı."));

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}