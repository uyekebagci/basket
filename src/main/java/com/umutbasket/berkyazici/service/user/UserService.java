package com.umutbasket.berkyazici.service.user; // PAKET YOLUNU GÜNCELLE

import com.umutbasket.berkyazici.dto.RegisterRequestDTO;
import com.umutbasket.berkyazici.dto.UpdateProfileRequestDTO;
import com.umutbasket.berkyazici.entity.Role;
import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.mapper.UserMapper;
import com.umutbasket.berkyazici.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    // Genel kayıt method
    public User createUser(RegisterRequestDTO request) {
        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setBirthDay(request.getBirthDay());
        newUser.setHeight(request.getHeight());
        newUser.setWeight(request.getWeight());
        newUser.setGender(request.getGender());
        newUser.setRole(Role.USER);

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Bu e-posta adresi zaten kullanılıyor.");
        }

        return userRepository.save(newUser);
    }

    // Kullanıcının kendi profilini güncelleme metodu
    public User updateUserProfile(UpdateProfileRequestDTO request) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User existingUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + currentUserEmail));
        userMapper.updateUserProfileFromDto(request, existingUser);
        return userRepository.save(existingUser);
    }
}