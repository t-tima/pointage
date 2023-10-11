package com.fatou.apiqr.controllers;

import com.fatou.apiqr.models.Presence;
import com.fatou.apiqr.models.PresenceDto;
import com.fatou.apiqr.services.PresenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@RestController
public class PresenceController {
    private final PresenceService presenceService;

    public PresenceController(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @PostMapping("/generateQRcode")
    public ResponseEntity<?> generateQRCodeAndRecordPresence() {
        return new ResponseEntity<>(presenceService.generateqr(), HttpStatus.OK);
    }

    @PostMapping("/ajouterPresence")
    public ResponseEntity<?> ajouterPresence(@RequestBody PresenceDto presenceDto) {
        return presenceService.ajouterPresence(presenceDto);
    }

    @GetMapping("/genererListe")
    public ResponseEntity<?> genererListePresence(@RequestParam String username) {
        return ResponseEntity.ok("Liste de présence générée avec succès");
    }

    @GetMapping("/getlistepresence")
    public List<Presence> getAllPresence(@RequestParam String username) {
        return presenceService.findAllByUsername(username);
    }

    @GetMapping("/presencedate")
    public ResponseEntity getAllPresence(@RequestParam String username, String dateDebut, String dateFin)  {
        try{

            SimpleDateFormat formatter1=new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = formatter1.parse(dateDebut);
            Date date2 = formatter1.parse(dateFin);
            List<Presence> presence =presenceService.getListPresence(username, date1, date2);
            return new ResponseEntity<>(presence , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Echec de recuperation des données", HttpStatus.valueOf(500));
        }

    }

}


