package com.umutbasket.berkyazici.Service;

import com.umutbasket.berkyazici.Entity.User;
import com.umutbasket.berkyazici.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser (User user) {
        return userRepository.save(user);
    }

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
