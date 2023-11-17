package com.fatou.apiqr.models;/*
 * @author
 * created on 04/10/2023
 * @project IntelliJ IDEA
 */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
@Entity
@Table(name ="users")
@Getter
@Setter
@NoArgsConstructor
public class UserModel {
    @Id
    @Column(length = 100)
    private String username;
    @Column (length = 100)
    private String password;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Date dateNaissance;
    @Column(length = 100)
    private String lieuNaissance;
    private Date createAt;
    private String role;
private boolean changer= false;
    private int code;


}