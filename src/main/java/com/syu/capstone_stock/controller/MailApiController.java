package com.syu.capstone_stock.controller;

import com.syu.capstone_stock.dto.EmailRequestDto;
import com.syu.capstone_stock.service.MailService;
import com.syu.capstone_stock.util.ClientUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3000, allowCredentials = "true")
public class MailApiController {
    private final MailService mailService;

    @PostMapping("/caps/mail/send")
    public ResponseEntity<?> sendMail(@RequestBody EmailRequestDto emailRequestDto, HttpServletRequest request) throws Exception {
//        return mailService.sendCertificationMail(emailRequestDto.getEmail());
        ClientUtils.getRemoteIP(request);
        return mailService.sendSimpleMessage(emailRequestDto);
    }

    @PostMapping("/caps/mail/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailRequestDto emailRequestDto, HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        return mailService.verifyCode(emailRequestDto);
    }

    @PostMapping("/caps/mail/cancel")
    public ResponseEntity<?> cancelVerifyEmail(@RequestBody EmailRequestDto emailRequestDto, HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        return mailService.cancelVerifyEmail(emailRequestDto);
    }
}
