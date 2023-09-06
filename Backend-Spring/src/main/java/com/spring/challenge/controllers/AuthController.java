package com.spring.challenge.controllers;

import javax.validation.Valid;

import com.spring.challenge.entities.Company;
import com.spring.challenge.entities.User;
import com.spring.challenge.entities.client;
import com.spring.challenge.payload.request.SignupRequest;
import com.spring.challenge.serviceimpl.UserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.spring.challenge.payload.request.LoginRequest;

import com.spring.challenge.payload.response.JwtResponse;
import com.spring.challenge.payload.response.MessageResponse;
import com.spring.challenge.repository.UserRepository;
import com.spring.challenge.security.jwt.JwtUtils;
import com.spring.challenge.security.services.UserDetailsImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

 @Autowired
    UserImpl Userservice;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    System.out.println(jwt);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


    return ResponseEntity.ok(new JwtResponse(jwt,
            "Bearer",
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(),
            userDetails.getRole()));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws NoSuchFieldException {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.ok(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return
      ResponseEntity.ok(new MessageResponse(" Error: Email is already in use!"));
    }

    // Create new user's account

    Userservice.addUser(signUpRequest);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }



    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        Userservice.store(file);
        System.out.println(String.format("File name '%s' uploaded successfully.", file.getOriginalFilename()));
        return ResponseEntity.ok().build();
    }



}
