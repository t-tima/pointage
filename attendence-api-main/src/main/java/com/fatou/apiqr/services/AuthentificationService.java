package com.fatou.apiqr.services;/*
 * @author
 * created on 04/10/2023
 * @project IntelliJ IDEA
 */

import com.fatou.apiqr.models.UserModel;
import com.fatou.apiqr.repositories.UserRepository;
import com.fatou.apiqr.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AuthentificationService {

    private final UserRepository userrepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailServiceImpl emailService;
    private final String password = "votre_mot_de_passe";
    private String random;



    public AuthentificationService(UserRepository userrepository, PasswordEncoder passwordEncoder, JwtService jwtService, EmailServiceImpl emailService) {
        this.userrepository = userrepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }


    public ResponseEntity login(UserModel login){
        log.info("LOGIN USER: {}", login.getUsername());
        UserModel user = userrepository.findByUsername(login.getUsername());
        if (user == null)
            return  new ResponseEntity<>("Login ou mot de passe invalide", HttpStatusCode.valueOf(400));

        if(!passwordEncoder.matches(login.getPassword(), user.getPassword())){
            return  new ResponseEntity<>("Echec authentification. Login ou mot de passe incorrect", HttpStatusCode.valueOf(400));
        }



        //TODO Implement JWT Token ACCESS
        Map<String, Object> response = new HashMap<>();
        String token = jwtService.createToken(user);
        response.put("user", user);
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    public ResponseEntity register(UserModel user){
        UserModel existingUser = userrepository.findByUsername(user.getUsername());
        if (existingUser != null)
            return  new ResponseEntity<>("Le username existe déjà !", HttpStatusCode.valueOf(400));

        emailService.sendPasswordToUser(user.getEmail(),"Votre mot de passe",user.getPassword());

        String encodedPass = passwordEncoder.encode(user.getPassword());
        log.info("ENCODED :{}", encodedPass);
        user.setPassword(encodedPass);
        user.setCreateAt(new Date());
        if("".equals(user.getRole()) ||  user.getRole() == null)
            user.setRole("USER");
        userrepository.save(user);
        return new ResponseEntity<>("Inscription effectuée avec succès", HttpStatus.OK);

    }
    public List<UserModel>  getAllUsers() {
        //List<UserModel> userModels = new ArrayList<UserModel>();
        return userrepository.findAll();
    }

    public ResponseEntity resetPassword(String email){
        UserModel existingUser = userrepository.findByEmail(email);
        if (existingUser == null)
            return  new ResponseEntity<>("L'email n'existe pas !", HttpStatusCode.valueOf(400));

       this.random = emailService.geneRandomPassword(email,"Change password");

        return new ResponseEntity<>( null, HttpStatusCode.valueOf(200));

    }

    public ResponseEntity modify(String username, String newpassword,String random){
        UserModel existingUser = userrepository.findByUsername(username);
        if (existingUser == null)
            return  new ResponseEntity<>("L'utilisateur n'existe pas !", HttpStatusCode.valueOf(400));

        if (!Objects.equals(String.valueOf(existingUser.getCode()), random))
            return  new ResponseEntity<>("Code incorrect !", HttpStatusCode.valueOf(400));


        String encodedPass = passwordEncoder.encode(newpassword);
        log.info("ENCODED :{}", encodedPass);
        existingUser.setPassword(encodedPass);
        userrepository.save(existingUser);


        return new ResponseEntity<>( null, HttpStatusCode.valueOf(200));

    }

}
