package com.fatou.apiqr.services;

import com.fatou.apiqr.models.UserModel;
import com.fatou.apiqr.repositories.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmailServiceImpl {


    private  final JavaMailSender emailSender;
    private  final UserRepository userRepository;


    public EmailServiceImpl(JavaMailSender emailSender, UserRepository userRepository) {
        this.emailSender = emailSender;
        this.userRepository = userRepository;
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("testdevv2023@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }

    public String geneRandomPassword(String email,String subject){
        UserModel userModel = userRepository.findByEmail(email);
        Random rand = new Random();
        int password = rand.nextInt(10000)*90;
        userModel.setCode(password);
        userModel.setChanger(true);
        this.userRepository.saveAndFlush(userModel);
        sendSimpleMessage(email, subject, String.valueOf(password));
        System.out.println("Mail envoyé " + password);
        return String.valueOf(password);
    }

    public String sendPasswordToUser(String email,String subject,String password ){

        sendSimpleMessage(email, subject, String.valueOf(password));
        System.out.println("Veuillez modifier votre mot de passe");
        return String.valueOf("veuillez changer votre mot de passe");


    }



}
