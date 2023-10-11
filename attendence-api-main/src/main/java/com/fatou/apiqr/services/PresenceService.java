package com.fatou.apiqr.services;

import com.fatou.apiqr.models.Presence;
import com.fatou.apiqr.models.PresenceDto;
import com.fatou.apiqr.models.QrCode;
import com.fatou.apiqr.repositories.PresenceRepository;
import com.fatou.apiqr.repositories.QrRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PresenceService {

    private static final int QR_EXPIRATION_TIME = 5 * 60 * 1000;
    @Autowired
    QrRepository qrRepository;
    @Autowired
    PresenceRepository presenceRepository;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void generateQRCodeAndRecordPresence() {
        Presence presence = new Presence();
        presence.setCreateAt(new Date());
        presence.setHeureEntrer(LocalTime.now());
        presence.setUpdatedDate(new Date());
        presence.setHeureSortie(LocalTime.now());

    }
    public QrCode generateqr() {
        QrCode qrCode = new QrCode();
        Date date = new Date();
        qrCode.setDatecreation(date);
        qrCode.setDateexpiration(new Date(date.getTime() + QR_EXPIRATION_TIME));
        qrCode.setCode("QRP_" + dateFormat.format(new Date()));
        qrRepository.save(qrCode);
        return qrCode;
    }

    public ResponseEntity ajouterPresence(PresenceDto presenceDto) {
        log.info("ADD Presence for user {}", presenceDto.getUsername());
        try {
            QrCode qrcode = qrRepository.findByIdqrcode(presenceDto.getIdqrcode());

            if (qrcode == null) {
                return new ResponseEntity("QR Code invalide", HttpStatus.valueOf(400));
            }

            if(!qrcode.getDateexpiration().after(new Date())){
                return new ResponseEntity("QR exprié ", HttpStatus.valueOf(400));
            }


            Presence presencetoday=presenceRepository.findFirstByUsernameAndCreateAt(presenceDto.getUsername(), getDateWithoutTimeUsingFormat());
            if (presencetoday!=null){
                log.info("UPDATE Presence: {}", presencetoday.getUsername());
                LocalTime heureEntrer = LocalTime.now();
                presencetoday.setHeureEntrer(heureEntrer);
                presencetoday.setUpdatedDate(new Date());
                double diff = heureEntrer.until(presencetoday.getHeureSortie(), ChronoUnit.HOURS);
                presencetoday.setCumul(diff);
                presenceRepository.saveAndFlush(presencetoday);
            }

            else{
            Presence presence = new Presence();
            presence.setIdqrcode(presenceDto.getIdqrcode());
            presence.setUsername(presenceDto.getUsername());
            presence.setCreateAt(this.getDateWithoutTimeUsingFormat());
            presence.setHeureSortie(LocalTime.now());

            presenceRepository.saveAndFlush(presence);
            }
            return new ResponseEntity("Presence ajouter avc succès", HttpStatus.OK);


        } catch (Exception e) {
            log.error("Error while adding presence {}", e.getMessage(), e);
            return new ResponseEntity("Echec ajout de presence", HttpStatus.valueOf(500));
        }

    }

    public List<Presence> findAllByUsername(String username) {
        List<Presence> presences = new ArrayList<Presence>();
        return presenceRepository.findAllByUsername(username);

    }

    public List<Presence> getListPresence(String username, Date dateDebut, Date  dateFin) {
        List<Presence> presences = new ArrayList<Presence>();
        return presenceRepository.findByUsernameAndCreateAtBetween(username, dateDebut, dateFin);
    }
    public Date getDateWithoutTimeUsingFormat()
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd-MM-yyyy");
        return formatter.parse(formatter.format(new Date()));
    }
}
