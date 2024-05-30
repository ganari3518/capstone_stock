package com.syu.capstone_stock.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {
    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @PrePersist
    public void prePersist(){
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.modifiedDate = LocalDateTime.now();
    }
}
