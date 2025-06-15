package com.umutbasket.berkyazici.service;

import com.umutbasket.berkyazici.dto.AuthenticationRequest;
import com.umutbasket.berkyazici.dto.AuthenticationResponse;
import com.umutbasket.berkyazici.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // 1. Spring Security'nin AuthenticationManager'ını kullanarak kullanıcıyı doğrula.
        //    Bu aşama, UserDetailsService'i çağırır ve parolaları kontrol eder.
        //    Eğer doğrulama başarısız olursa, burada bir exception fırlatılır.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Eğer doğrulama başarılıysa, veritabanından kullanıcıyı bul.
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(); // Doğrulama başarılı olduğu için burada kullanıcı kesinlikle bulunur.

        // 3. Bulunan kullanıcı için bir JWT üret.
        var jwtToken = jwtService.generateToken((UserDetails) user);

        // 4. Üretilen token'ı AuthenticationResponse DTO'su içinde geri döndür.
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}