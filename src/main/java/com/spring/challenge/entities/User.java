package com.spring.challenge.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Entity
public class User {
  @Id
  @GeneratedValue

  private Long id;


  @Size(max = 20)
  private String username;


  @Size(max = 50)
  @Email
  private String email;


  @Size(max = 120)
  private String password;


  @Size(max = 15)
  private String Pays;


  @Size(max = 5)
  private String CodePostal;


  @Size(max = 20)
  private String Region;

  @Enumerated
  private ERole role ;

  public User() {
  }


  }

