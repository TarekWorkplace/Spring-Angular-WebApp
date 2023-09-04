package com.spring.challenge.repository;

import com.spring.challenge.entities.Company;
import com.spring.challenge.entities.client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcheteurRepository extends JpaRepository<client, Long> {


    Optional<Company> findByUsername(String username);

}