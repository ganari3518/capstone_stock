package com.syu.capstone_stock.controller;

import com.syu.capstone_stock.dto.EmailRequestDto;
import com.syu.capstone_stock.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MailApiController {
    private final MailService mailService;

    @PostMapping("/caps/mail/send")
    public ResponseEntity<?> sendMail(@RequestBody EmailRequestDto emailRequestDto) throws Exception {
//        return mailService.sendCertificationMail(emailRequestDto.getEmail());
        return mailService.sendSimpleMessage(emailRequestDto);
    }

    @PostMapping("/caps/mail/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailRequestDto emailRequestDto){
        return mailService.verifyCode(emailRequestDto);
    }

    @PostMapping("/caps/mail/cancel")
    public ResponseEntity<?> cancelVerifyEmail(@RequestBody EmailRequestDto emailRequestDto){
        return mailService.cancelVerifyEmail(emailRequestDto);
    }
}
