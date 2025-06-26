package com.Linkdin.linkdinbackend.features.authentication.repository;

import java.util.List;
import java.util.Optional;

import com.Linkdin.linkdinbackend.features.authentication.model.AuthenticationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AuthenticationUserRepository extends JpaRepository<AuthenticationUser, Long> {
    Optional<AuthenticationUser> findByEmail(String email);

    List<AuthenticationUser> findAllByIdNot(Long id);
}
