package com.syu.capstone_stock.repositry;

import com.syu.capstone_stock.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<Email, String> {
    Email findByEmailAndMailauth(String email, boolean auth);
    Email findByEmailAndMailkey(String email, String mailkey);

    Email findByEmail(String email);
}
