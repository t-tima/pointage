package com.fatou.apiqr.controllers;

import com.fatou.apiqr.models.UserModel;
import com.fatou.apiqr.services.AuthentificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private  final AuthentificationService authentificationService;

    public UserController(AuthentificationService authentificationService) {
        this.authentificationService = authentificationService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody UserModel user){
        return authentificationService.login(user);
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody UserModel user){
        System.out.println(user);
        return authentificationService.register(user);
    }

    @GetMapping("userliste")
    public List<UserModel> getAllUsers() {
        return authentificationService.getAllUsers();
    }
}
