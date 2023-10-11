package com.fatou.apiqr.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;
import java.util.Date;
@Entity
@Table(name = "presence")
@Getter
@Setter
@NoArgsConstructor
public class Presence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idpresence;
    private LocalTime heureEntrer;
    private Date updatedDate;
    private Double cumul;
    private LocalTime heureSortie;
    private Date createAt;
    private String username;
    private Integer idqrcode;

}
