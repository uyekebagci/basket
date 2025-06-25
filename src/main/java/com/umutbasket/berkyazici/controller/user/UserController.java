package com.umutbasket.berkyazici.controller.user;

import com.umutbasket.berkyazici.dto.UpdateProfileRequestDTO;
import com.umutbasket.berkyazici.dto.UserResponseDTO;
import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.mapper.UserMapper;
import com.umutbasket.berkyazici.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user") // Bu controller'daki tüm yollar /api/user ile başlayacak
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    // Kullanıcının kendi profilini güncellemesi için
    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(@RequestBody UpdateProfileRequestDTO request) {
        User updatedUser = userService.updateUserProfile(request);
        UserResponseDTO responseDTO = userMapper.toUserResponseDTO(updatedUser);
        return ResponseEntity.ok(responseDTO);
    }
}