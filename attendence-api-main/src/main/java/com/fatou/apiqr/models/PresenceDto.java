package com.fatou.apiqr.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PresenceDto {
    private Integer idqrcode;
    private String Code;
    private String username;
}
