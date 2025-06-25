package com.umutbasket.berkyazici.controller.admin;

import com.umutbasket.berkyazici.dto.UpdateUserRequestDTO;
import com.umutbasket.berkyazici.dto.UserResponseDTO;
import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.mapper.UserMapper; // YENİ IMPORT
import com.umutbasket.berkyazici.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final UserMapper userMapper; // YENİ: Mapper'ı enjekte ediyoruz

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = adminUserService.getAllUsers();
        // Artık dönüşüm işini mapper yapıyor
        List<UserResponseDTO> response = userMapper.toUserResponseDTOList(users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{firstName}")
    public ResponseEntity<UserResponseDTO> getClientByFirstName(@PathVariable String firstName) {
        Optional<User> userOptional = adminUserService.getClientByFirstName(firstName);
        log.info("Users has queried according to firstName");

        return userOptional
                .map(userMapper::toUserResponseDTO) // Metot referansı ile mapper'ı kullanıyoruz
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update/{userId}") // ID'yi path'ten almak daha standarttır
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequestDTO request) {
        User updatedUser = adminUserService.updateUser(userId, request); // Servis metodunu da güncelleyeceğiz
        log.info("User with ID: {} has been updated", userId);
        return new ResponseEntity<>(userMapper.toUserResponseDTO(updatedUser), HttpStatus.OK);
    }
}