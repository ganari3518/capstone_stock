package com.syu.capstone_stock.dto;

import com.syu.capstone_stock.domain.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class EmailRequestDto {
    private String email;
    private String mailkey;

    public Email toEntity(String key){
        return Email.builder()
                .uuid(UUID.randomUUID().toString())
                .email(this.email)
                .mailkey(key)
                .mailauth(false)
                .created_date(LocalDateTime.now())
                .build();
    }
}