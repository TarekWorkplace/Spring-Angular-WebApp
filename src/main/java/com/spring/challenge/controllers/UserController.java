package com.spring.challenge.controllers;

import com.spring.challenge.entities.Company;
import com.spring.challenge.entities.Operateur;
import com.spring.challenge.entities.User;
import com.spring.challenge.entities.client;
import com.spring.challenge.serviceimpl.UserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {



  @Autowired
  UserImpl userservice;



  @DeleteMapping("delete/{id}")
  public void deleteUser(@PathVariable("id") Long UserId) {

    userservice.deleteUser(UserId);
  }



  @GetMapping("/all/{Role}")
  public List getJobs(@PathVariable("Role") String Role) {
    List list=new ArrayList<>();


    switch (Role)
    {
      case "Rôle_Fournisseur":
        list = userservice.UserCompanies();
        break;

      case "Rôle_Acheteur" :
        list = userservice.retrieveUser();
        break;

      case "Rôle_Opérateur":
        list = userservice.UserOperateur();
        break;


    }

    return list;
  }


  @PutMapping("updateClient/{id}")
  @ResponseBody
  public client updateClient(@PathVariable("id") Long id, @RequestBody client cl) {
    client client = userservice.updateClientt(id,cl);
    return client;
  }

  @PutMapping("updateCompany/{id}")
  @ResponseBody
  public Company updateCompany(@PathVariable("id") Long id, @RequestBody Company c ) {
    Company company = userservice.updateCompany(id,c);
    return company;
  }

  @PutMapping("updateOperatuer/{id}")
  @ResponseBody
  public Operateur updateOperateur(@PathVariable("id") Long id, @RequestBody Operateur o) {
    Operateur operateur = userservice.updateOperatuer(id,o);
    return operateur;
  }





  @GetMapping("/user")
  @PreAuthorize("hasRole('ROLE_SEEKER') or hasRole('ROLE_COMPANY') ")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/company")
  @PreAuthorize("hasRole('ROLE_COMPANY')")
  public String moderatorAccess() {
    return "Company.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ROLE_SEEKER')")
  public String adminAccess() {
    return "Cleint.";
  }
}
