package com.spring.challenge.repository;

import com.spring.challenge.entities.Operateur;
import com.spring.challenge.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperateurRepository extends JpaRepository<Operateur, Long> {

  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);


}
