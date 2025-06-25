package com.umutbasket.berkyazici.config;

import com.umutbasket.berkyazici.entity.Role;
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
import org.springframework.security.config.http.SessionCreationPolicy; // EKSİK IMPORT
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

 private final JwtAuthenticationFilter jwtAuthFilter;
 private final UserDetailsServiceImpl userDetailsService;

 @Bean
 public PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder();
 }

 @Bean
 public AuthenticationProvider authenticationProvider() {
  DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
  authProvider.setUserDetailsService(userDetailsService);
  authProvider.setPasswordEncoder(passwordEncoder());
  return authProvider;
 }

 @Bean
 public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
  return config.getAuthenticationManager();
 }

 @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  http
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(req ->
                  req.requestMatchers("/api/public/**")
                          .permitAll()
                          .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
                          .anyRequest()
                          .authenticated()
          )
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authenticationProvider(authenticationProvider())
          .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

  return http.build();
 }
}