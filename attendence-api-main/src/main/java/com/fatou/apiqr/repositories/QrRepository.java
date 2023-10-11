package com.fatou.apiqr.repositories;

import com.fatou.apiqr.models.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QrRepository extends JpaRepository<QrCode,Integer> {
    QrCode findByIdqrcode (Integer idqrcode);
}
