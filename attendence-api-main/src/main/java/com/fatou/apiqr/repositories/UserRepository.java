package com.fatou.apiqr.repositories;/*
 * @author
 * created on 04/10/2023
 * @project IntelliJ IDEA
 */

import com.fatou.apiqr.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, String> {
    UserModel findByUsername(String username);
}
