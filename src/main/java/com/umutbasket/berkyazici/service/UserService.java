package com.umutbasket.berkyazici.service;

import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.repository.UserRepository;
import lombok.RequiredArgsConstructor; // YENİ IMPORT
import org.springframework.security.crypto.password.PasswordEncoder; // YENİ IMPORT
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // @Autowired yerine bunu kullanmak daha modern
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder'ı enjekte et

    // @Autowired constructor'ı silebilirsin, @RequiredArgsConstructor bunu halleder.

    public User createUser(User user) {
        // Parolayı şifreleyerek kaydet
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser (User user) {
        // TODO: Parola güncellemesi için de şifreleme eklenmeli
        return userRepository.save(user);
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