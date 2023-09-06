package com.spring.challenge.serviceimpl;
import java.lang.Long;
import com.spring.challenge.entities.Company;
import com.spring.challenge.entities.Operateur;
import com.spring.challenge.entities.User;
import com.spring.challenge.entities.client;
import com.spring.challenge.repository.*;
import com.spring.challenge.repository.UserRepository;
import com.spring.challenge.service.UserI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import com.spring.challenge.payload.request.SignupRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service

public class UserImpl implements UserI {
    private final Path rootLocation= Paths.get("upload-dir");
    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private OperateurRepository operateurRepository;

    @Autowired
    private AcheteurRepository acheteurRepository;



    @Autowired
    PasswordEncoder encoder;



    @Override
    public List<User> retrieveAllUser() {
        List<User> Users = (List<User>) UserRepository.findAll();
        return Users;
    }

    @Override
    public User addUser(SignupRequest signUpRequest) throws NoSuchFieldException {

        User user = new User() ;
        user.setEmail(signUpRequest.getEmail());
        user.setPassword( encoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole());
        String role = String.valueOf(signUpRequest.getRole());


        switch (role) {
            case "Rôle_Fournisseur":
                Company company = new Company();

                company.setEmail(signUpRequest.getEmail());
                company.setPassword( encoder.encode(signUpRequest.getPassword()));
                company.setRole(signUpRequest.getRole());
                company.setUsername(signUpRequest.getUsername());

                companyRepository.save(company);

                break;
            case "Rôle_Acheteur":

                client client  = new client();

                client.setEmail(signUpRequest.getEmail());
                client.setPassword( encoder.encode(signUpRequest.getPassword()));
                client.setRole(signUpRequest.getRole());
                client.setUsername(signUpRequest.getUsername());
                acheteurRepository.save(client);
                break;


            case "Rôle_Opérateur":
                Operateur operateur = new Operateur();

                operateur.setEmail(signUpRequest.getEmail());
                operateur.setPassword( encoder.encode(signUpRequest.getPassword()));
                operateur.setRole(signUpRequest.getRole());
                operateur.setUsername(signUpRequest.getUsername());

                operateurRepository.save(operateur);

                break;

        }
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        UserRepository.deleteById(id);
    }

    @Override
    public client updateClientt(Long id,client cl) {

        client client=acheteurRepository.findById(id).orElse(null);

        client.setCodePostal(cl.getCodePostal());

        client.setRegion(cl.getRegion());

        client.setGender(cl.getGender());

        client.setPays(cl.getPays());

        return acheteurRepository.save(client);
    }

    @Override
    public Operateur updateOperatuer(Long id,Operateur O) {



        Operateur operateur=operateurRepository.findById(id).orElse(null);

        operateur.setCodePostal(O.getCodePostal());

        operateur.setRegion(O.getRegion());

        operateur.setGender(O.getGender());

        operateur.setPays(O.getPays());

        return operateurRepository.save(operateur);
    }

    @Override
    public Company updateCompany(Long id,Company C) {

        Company company=companyRepository.findById(id).orElse(null);

        company.setCodePostal(C.getCodePostal());

        company.setRegion(C.getRegion());

        company.setProductType(C.getProductType());

        company.setPays(C.getPays());

        return companyRepository.save(company);
    }


    @Override
    public User findByIdUser(Long id) {
        User User = UserRepository.findById(id).orElse(null);
        return User;
    }

    @Override
    public List<Company>UserCompanies() {

        List<Company> companies = (List<Company>) companyRepository.findAll();
        return companies;


    }

    @Override
    public List UserOperateur() {

        List<Operateur> companies = (List<Operateur>) operateurRepository.findAll();
        return companies;


    }

    @Override
    public List retrieveUser() {
        List list= acheteurRepository.findAll();
        return list;
    }

    @Override
    public void store(MultipartFile file) throws IOException {

            if (file.isEmpty()) {
               System.out.println("File is empty");

        }
        Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
    }


    public List<Object> getCities() throws NoSuchFieldException {
        String url="https://geo.api.gouv.fr/departements/94/communes?fields=nom"; // change department number here
        RestTemplate restTemplate= new RestTemplate();
        Object cities = restTemplate.getForObject(url,Object.class);

        return Arrays.asList(cities);
    }

}
