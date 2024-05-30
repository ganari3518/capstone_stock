package com.syu.capstone_stock.service;

import com.syu.capstone_stock.domain.Email;
import com.syu.capstone_stock.dto.EmailRequestDto;
import com.syu.capstone_stock.repositry.MailRepository;
import com.syu.capstone_stock.repositry.MemberRepository;
import com.syu.capstone_stock.util.RequestTimeTracker;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class MailService{
    private final JavaMailSender emailSender;
    private final MemberRepository memberRepository;
    private final MailRepository mailRepository;

    private String key;

    @Value("${Mail}")
    private String id;

    private Instant codeGenerationTime;

    private Duration validityDuration = Duration.ofMinutes(1);

    public ResponseEntity<?> sendSimpleMessage(EmailRequestDto to) {
        try{
            if(memberRepository.findByEmailAndMailauth(to.getEmail(), true) != null){
                return ResponseEntity.badRequest().body("이미 인증된 이메일입니다.");
            }

            if(RequestTimeTracker.canProcessRequest(getClientIp(), 60)){
                MimeMessage message = createMessage(to);
                emailSender.send(message);
            } else{
                return ResponseEntity.badRequest().body("1분 뒤에 다시 시도할 수 있습니다.");
            }

        } catch (Exception e){
            return ResponseEntity.internalServerError().body("메일 서비스가 정상적으로 동작하지 않습니다.");
        }
        return ResponseEntity.ok("메일을 성공적으로 보냈습니다.");
    }

    private String getClientIp(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null){
            HttpServletRequest request = attributes.getRequest();
            return request.getRemoteAddr();
        }
        return null;
    }

    private MimeMessage createMessage(EmailRequestDto to) throws Exception {
        key = createKey();
        codeGenerationTime = Instant.now();

        Email email = mailRepository.findByEmail(to.getEmail());
        if(email == null){
            mailRepository.save(to.toEntity(key));
        } else {
            email.setMailkey(key);
            mailRepository.save(email);
        }

        System.out.println(key);

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to.getEmail());
        message.setSubject("회원가입을 위한 이메일 인증코드");

        String msgg = "";
        msgg += "<div align='center' style='border:1px solid black'>";
        msgg += "<h3 style='color:blue'>회원가입 인증코드 입니다</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "<strong>" + key + "</strong></div><br/>";
        msgg += "<p>유효 시간: " + validityDuration.toMinutes() + "분 동안만 유효합니다.</p>";
        msgg += "</div>";

        message.setText(msgg, "utf-8", "html");
        message.setFrom(id);

        return message;
    }

    private String createKey() throws Exception{
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < length; i++){
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e){
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<?> verifyCode(EmailRequestDto emailRequestDto){
        System.out.println(emailRequestDto.getEmail()+", "+emailRequestDto.getMailkey());
        try {
            if(codeGenerationTime == null){
//                return ApiResponse.createFail(HttpStatus.BAD_REQUEST.value(),"시간 정보가 없습니다.");
                return ResponseEntity.internalServerError().body("시간 정보가 없습니다.");
            }
            Duration elapsedDuration = Duration.between(codeGenerationTime, Instant.now());
            long remainDuration = validityDuration.minus(elapsedDuration).getSeconds();

            if(remainDuration > 0){
                if(emailRequestDto.getMailkey().equals(mailRepository.findByEmail(emailRequestDto.getEmail()).getMailkey())){
//                    return ApiResponse.createSuccess("인증 번호가 일치합니다.");
                    Email email = mailRepository.findByEmailAndMailkey(emailRequestDto.getEmail(), emailRequestDto.getMailkey());
                    email.setMailauth(true);
                    mailRepository.save(email);
                    return ResponseEntity.ok().body("인증 번호가 일치합니다.");
                } else{
//                    return ApiResponse.createFail(HttpStatus.BAD_REQUEST.value(), "인증 번호가 일치하지 않습니다.");
                    return ResponseEntity.badRequest().body("인증 번호가 일치하지 않습니다.");
                }
            } else if (remainDuration < 0) {
//                return ApiResponse.createFail(HttpStatus.BAD_REQUEST.value(), "인증 번호의 유효시간이 지났습니다.");
                return ResponseEntity.badRequest().body("인증 번호의 유효시간이 지났습니다.");
            }
//            return ApiResponse.createFail(HttpStatus.BAD_REQUEST.value(), "잘못된 접근입니다.");
            return ResponseEntity.badRequest().body("잘못된 접근입니다.");
        } catch (NullPointerException e){
//            return ApiResponse.createFail(HttpStatus.BAD_REQUEST.value(), "유효하지 않는 인증 번호를 입력했습니다.");
            return ResponseEntity.badRequest().body("유효하지 않는 인증 번호를 입력했습니다.");
        }
    }

    public ResponseEntity<?> cancelVerifyEmail(EmailRequestDto emailRequestDto){
        if(mailRepository.findByEmailAndMailkey(emailRequestDto.getEmail(), emailRequestDto.getMailkey()) != null){
            Email email = mailRepository.findByEmailAndMailkey(emailRequestDto.getEmail(), emailRequestDto.getMailkey());
            email.setMailauth(false);
            mailRepository.save(email);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("잘못된 접근입니다.");
        }
    }
}

//    public ResponseEntity<?> sendSimpleMessage(EmailRequestDto to) {
//        try{
//            if(memberRepository.findByEmailAndMailauth(to.getEmail(), true) != null){
//                return ResponseEntity.badRequest().body("이미 인증된 이메일입니다.");
//            }
//            MimeMessage message = createMessage(to);
//            emailSender.send(message);
//        } catch (Exception e){
//            return ResponseEntity.internalServerError().body("메일 서비스가 정상적으로 동작하지 않습니다.");
//        }
//        return ResponseEntity.ok("메일을 성공적으로 보냈습니다.");
//    }

//    private static ThreadLocal<Instant> requestThread = new ThreadLocal<>();
//    private boolean checkDuration(){
////        Instant requestTime = requestThread.get();
////
////        if(requestTime == null){
////            requestTime = Instant.now();
////            return true;
////        }
////
////        Duration elapsedDuration = Duration.between(requestTime, Instant.now());
////
////        if(elapsedDuration.getSeconds() >= 60){
////            requestTime = Instant.now();
////            return true;
////        } else {
////            return false;
////        }
//        Instant requestTime = requestThread.get();
//
//        if(requestTime == null){
//            requestThread.set(Instant.now());
//            return true;
//        }
//
//        Duration elapsedDuration = Duration.between(requestTime, Instant.now());
//
//        if(elapsedDuration.getSeconds() >= 60){
//            requestThread.remove();
//            requestThread.set(Instant.now());
//            return true;
//        } else {
//            return false;
//        }
//    }
