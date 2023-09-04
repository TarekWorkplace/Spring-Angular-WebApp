package com.spring.challenge.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
public class Operateur extends User{



    @Size(max = 10)
    private String Gender;

    private boolean status;


}
