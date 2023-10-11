package com.fatou.apiqr.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "qrcode")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idqrcode;
    private Date datecreation;
    private Date dateexpiration;
    private String code;


}
