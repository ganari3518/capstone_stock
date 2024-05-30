package com.syu.capstone_stock.dto;

import com.syu.capstone_stock.domain.Gender;
import com.syu.capstone_stock.domain.Member;
import com.syu.capstone_stock.util.EncryptionUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequestDto {
    private Long id;
    private String loginId;
    private String email;
    private String password;
    private String name;
    private String regNo;
    private Gender gender;
    private String mailkey;

    public Member toEntity(){
        return Member.builder()
                .loginId(this.loginId)
                .password(EncryptionUtil.sha256Encode(this.password))
                .email(this.email)
                .name(this.name)
                .regNo(EncryptionUtil.aesEncode(this.regNo))
                .gender(this.gender)
                .deleteYn(false)
                .createdDate(LocalDateTime.now())
                .mailauth(true)
                .mailkey(mailkey)
                .build();
    }
}
