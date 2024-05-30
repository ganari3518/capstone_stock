package com.syu.capstone_stock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_auth")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Email {
    @Id
    private String uuid;

    private String email;

    private String mailkey;

    private boolean mailauth;

    private LocalDateTime created_date;

//    private LocalDateTime expire_date;
}
