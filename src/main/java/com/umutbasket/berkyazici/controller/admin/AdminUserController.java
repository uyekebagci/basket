package com.umutbasket.berkyazici.controller.admin;

import com.umutbasket.berkyazici.dto.UserResponseDTO;
import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        // Entity listesini DTO listesine çevir
        List<UserResponseDTO> response = users.stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{firstName}")
    public ResponseEntity<UserResponseDTO> getClientByFirstName(@PathVariable String firstName) {
        Optional<User> userOptional = userService.getClientByFirstName(firstName);
        log.info("Users has queried according to firstName");
        // Optional<User> nesnesini Optional<UserResponseDTO>'ya çevir
        return userOptional
                .map(this::convertToUserResponseDTO)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        log.info("User has updated");
        return new ResponseEntity<>(convertToUserResponseDTO(updatedUser), HttpStatus.OK);
    }

    private UserResponseDTO convertToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .birthDay(user.getBirthDay())
                .height(user.getHeight())
                .weight(user.getWeight())
                .age(user.getAge())
                .gender(user.getGender())
                .role(user.getRole())
                .plan(user.getPlan())
                .build();
    }

}
