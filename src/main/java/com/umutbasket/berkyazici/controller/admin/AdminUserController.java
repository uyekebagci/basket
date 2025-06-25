package com.umutbasket.berkyazici.controller.admin;

import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{firstName}")
    public ResponseEntity<Optional<User>> getClientByFirstName(@PathVariable String firstName) {
        Optional<User> user = userService.getClientByFirstName(firstName);
        log.info("Users has queried according to firstName");
        if (user.isPresent()) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        log.info("User has updated");
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}
