package com.syu.capstone_stock.controller;

import com.syu.capstone_stock.domain.Member;
import com.syu.capstone_stock.dto.MemberRequestDto;
import com.syu.capstone_stock.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/signup")
    @ResponseBody
    public Long signUp(@RequestBody final MemberRequestDto memberRequestDto){
        return memberService.saveMember(memberRequestDto);
    }

    @GetMapping("/login.do")
    public String openLogin(){
        return "/member/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Member login(HttpServletRequest request){
        Member m = memberService.login(request.getParameter("loginId"),request.getParameter("password"));

        if(m != null){
            HttpSession session = request.getSession();
            session.setAttribute("loginMember", m);
            session.setMaxInactiveInterval(60*30);
        }

        return m;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/setting/list.do")
    public String openSetting(){
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
