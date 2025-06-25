package com.umutbasket.berkyazici.service.admin;

import com.umutbasket.berkyazici.dto.UpdateUserRequestDTO;
import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.mapper.AdminUserMapper; // DEĞİŞİKLİK: Doğru mapper import edildi
import com.umutbasket.berkyazici.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    // DEĞİŞİKLİK: Artık AdminUserMapper enjekte ediliyor
    private final AdminUserMapper adminUserMapper;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> getClientByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long userId, UpdateUserRequestDTO request) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // DEĞİŞİKLİK: Doğru mapper metodu çağrılıyor
        adminUserMapper.updateUserFromDto(request, existingUser);

        return userRepository.save(existingUser);
    }
}