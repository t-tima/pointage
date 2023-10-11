package com.fatou.apiqr.repositories;

import com.fatou.apiqr.models.Presence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PresenceRepository extends JpaRepository<Presence,Long> {
    Presence findByIdpresence (Long idPresence);
    List<Presence> findAllByUsername (String username);
    List<Presence> findByUsernameAndCreateAtBetween (String username, Date createAt, Date createAt2);
    Presence findFirstByCreateAt (Date createAt);
    Presence findFirstByUsernameAndCreateAt (String username, Date createAt);
}
