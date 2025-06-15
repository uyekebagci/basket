package com.umutbasket.berkyazici.repository;

import com.umutbasket.berkyazici.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByFirstName(String firstName);

    Optional<Object> findByEmail(String email);
}
