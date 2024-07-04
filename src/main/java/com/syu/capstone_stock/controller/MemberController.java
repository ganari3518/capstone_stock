package com.syu.capstone_stock.controller;

import com.syu.capstone_stock.domain.Member;
import com.syu.capstone_stock.dto.MemberRequestDto;
import com.syu.capstone_stock.service.MemberService;
import com.syu.capstone_stock.util.ClientUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3000, allowCredentials = "true")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/signup")
    @ResponseBody
    public ResponseEntity<?> signUp(@RequestBody final MemberRequestDto memberRequestDto, HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        return memberService.saveMember(memberRequestDto);
    }

    @GetMapping("/login.do")
    public String openLogin(HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        return "/member/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Member login(HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        Member m = memberService.login(request.getParameter("loginId"),request.getParameter("password"));

        if(m != null){
            HttpSession session = request.getSession();
            session.setAttribute("loginMember", m);
            session.setMaxInactiveInterval(60*30);
        }

        return m;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/setting/list.do")
    public String openSetting(HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        return "/setting/list";
    }

//    @GetMapping("/members/{loginId}")
//    @ResponseBody
//    public Member findMemberByLoginId(@PathVariable final String loginId){
//        Member m = memberService.findMemberByLoginId(loginId);
//        m.setPassword(null);
//        m.setRegNo(null);
//        return m;
//    }

//    @PatchMapping("/members/{id}")
//    @ResponseBody
//    public Long updateMember(@PathVariable final Long id, @RequestBody final MemberRequestDto params){
//        return memberService.updateMember(params);
//    }

//    @DeleteMapping("/members/{id}")
//    @ResponseBody
//    public Long deleteMemberById(final Long id){
//        return memberService.deleteMemberById(id);
//    }

    @GetMapping("/member-count")
    @ResponseBody
    public int countMemberByLoginId(@RequestParam final String loginId){
        return memberService.countMemberByLoginId(loginId);
    }
}
