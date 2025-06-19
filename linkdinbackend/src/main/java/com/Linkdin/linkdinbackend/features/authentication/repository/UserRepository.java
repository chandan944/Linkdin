package com.Linkdin.linkdinbackend.features.authentication.repository;

import java.util.List;
import java.util.Optional;

import com.Linkdin.linkdinbackend.features.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findAllByIdNot(Long id);
}
