package com.umutbasket.berkyazici.config;

import com.umutbasket.berkyazici.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // @Autowired yerine constructor injection için
public class SecurityConfig {

 private final UserDetailsServiceImpl userDetailsService; // Kendi UserDetailsService'imiz

 private final JwtAuthenticationFilter jwtAuthFilter;

 @Bean
 public PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder();
 }


 /**
  * YENİ: AuthenticationProvider Bean'i
  * Bu bean, Spring Security'e kullanıcı detaylarını nereden alacağını (userDetailsService)
  * ve parolaları nasıl karşılaştıracağını (passwordEncoder) söyler.
  */
 @Bean
 public AuthenticationProvider authenticationProvider() {
  DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
  authProvider.setUserDetailsService(userDetailsService);
  authProvider.setPasswordEncoder(passwordEncoder());
  return authProvider;
 }

 /**
  * YENİ: AuthenticationManager Bean'i
  * Spring Security 6+ ile birlikte AuthenticationManager'ı doğrudan bu şekilde alıyoruz.
  * Bu, AuthenticationService'de kullandığımız ana kimlik doğrulama mekanizmasıdır.
  */
 @Bean
 public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
  return config.getAuthenticationManager();
 }

 @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  http
          .csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/api/users/create", "/api/auth/**").permitAll()
                  .anyRequest().authenticated()
          )
          // YENİ: Session yönetimini stateless (durumsuz) olarak ayarlıyoruz.
          // JWT kullandığımız için sunucuda session tutulmasına gerek yok.
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authenticationProvider(authenticationProvider())
          // YENİ: Oluşturduğumuz JWT filtresini, standart kullanıcı adı/parola filtresinden
          // ÖNCE çalışacak şekilde zincire ekliyoruz.
          .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

  return http.build();
 }

}