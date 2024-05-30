package com.syu.capstone_stock.dto;

import com.syu.capstone_stock.domain.Post;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostRequestDto {
    private Long id;
    private String service;
    private String apikey;
    private String email;

    public Post toEntity(){
        return Post.builder()
                .service(this.service)
                .apikey(this.apikey)
                .email(this.email)
                .deleteYn(false)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
