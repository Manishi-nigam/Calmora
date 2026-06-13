package com.calmora.repository;

import com.calmora.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    /** Find user by username */
    Optional<User> findByUsername(String username);



    /** Check if username already exists */
    boolean existsByUsername(String username);

    /** Check if email already exists */
    boolean existsByEmail(String email);
}
