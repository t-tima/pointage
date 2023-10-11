package com.fatou.apiqr.controllers;

import com.fatou.apiqr.models.UserModel;
import com.fatou.apiqr.services.AuthentificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return authentificationService.register(user);

    }
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
