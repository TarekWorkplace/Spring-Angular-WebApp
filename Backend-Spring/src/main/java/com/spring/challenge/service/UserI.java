package com.spring.challenge.service;

import com.spring.challenge.entities.Company;
import com.spring.challenge.entities.Operateur;
import com.spring.challenge.entities.User;
import com.spring.challenge.entities.client;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.spring.challenge.payload.request.SignupRequest;
import java.io.IOException;
import java.util.List;

public interface UserI {

    List<User> retrieveAllUser();

    User addUser(SignupRequest SignupRequest) throws NoSuchFieldException;

    @Transactional
    void deleteUser(Long j);

    client updateClientt(Long id,client cl);

    Operateur updateOperatuer(Long id,Operateur O);

    Company updateCompany(Long id,Company C);

    User findByIdUser(Long id);


    List UserCompanies();

    List UserOperateur();
    List  retrieveUser();

    void store(MultipartFile file) throws IOException;



}
