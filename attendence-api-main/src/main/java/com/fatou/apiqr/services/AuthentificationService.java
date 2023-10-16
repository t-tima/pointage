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

    public AuthentificationService(UserRepository userrepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userrepository = userrepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

        String encodedPass = passwordEncoder.encode(user.getPassword());
        log.info("ENCODED :{}", encodedPass);
        user.setPassword(encodedPass);
        user.setCreateAt(new Date());
        user.setRole("USER");
        userrepository.save(user);
        return new ResponseEntity<>("Inscription effectuée avec succès", HttpStatus.OK);

    }
    public List<UserModel>  getAllUsers() {
        //List<UserModel> userModels = new ArrayList<UserModel>();
        return userrepository.findAll();
    }
}
