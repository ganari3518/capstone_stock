package com.syu.capstone_stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class PostIgnore {
    @JsonIgnore
    private String email;

    @JsonIgnore
    private Boolean deleteYn;

    @JsonIgnore
    private LocalDateTime modifiedDate;
}
