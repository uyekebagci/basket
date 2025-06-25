package com.umutbasket.berkyazici.service;

import com.umutbasket.berkyazici.dto.UpdateUserRequestDTO;
import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.umutbasket.berkyazici.dto.RegisterRequestDTO;
import com.umutbasket.berkyazici.entity.Role;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // @Autowired yerine bunu kullanmak daha modern
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder'ı enjekte et

    public User createUser(RegisterRequestDTO request) {
        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Parolayı şifrele
        newUser.setBirthDay(request.getBirthDay());
        newUser.setHeight(request.getHeight());
        newUser.setWeight(request.getWeight());
        newUser.setGender(request.getGender());
        newUser.setRole(Role.USER); // Her zaman varsayılan olarak USER rolü ata

        return userRepository.save(newUser);
    }

    public User updateUser(Long userId, UpdateUserRequestDTO request) {
        // Önce kullanıcıyı bul
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)); // TODO: Özel exception sınıfı oluşturulmalı

        // Sadece DTO'da gelen ve null olmayan alanları güncelle
        if (request.getFirstName() != null) existingUser.setFirstName(request.getFirstName());
        if (request.getLastName() != null) existingUser.setLastName(request.getLastName());
        if (request.getEmail() != null) existingUser.setEmail(request.getEmail());
        if (request.getBirthDay() != null) existingUser.setBirthDay(request.getBirthDay());
        // ... diğer tüm alanlar için aynı kontrol ...
        if (request.getRole() != null) existingUser.setRole(request.getRole());

        return userRepository.save(existingUser);
    }

    // ... (diğer metotlar aynı kalabilir)
    @Transactional(readOnly = true)
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUsers () {
        return new User();
    }

    public Optional<User> getClientByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }
}