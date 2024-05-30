package com.syu.capstone_stock.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_member")
@Builder
@AllArgsConstructor
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String password;

    private String email;

    private String name;

    private String regNo;

    @Enumerated(value = EnumType.STRING)    //Enum설정 안할 경우 db에 값이 정상적으로 들어가지 않으며, 조회에서 ArrayIndexOutOfBoundException에러. insert는 되긴함.
    private Gender gender;

    private Boolean deleteYn;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private Boolean mailauth;

    private String mailkey;
}