package com.syu.capstone_stock.controller;

import com.syu.capstone_stock.util.ClientUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3000, allowCredentials = "true")
public class StockController {
    @GetMapping("/")
    public String openIndex(HttpServletRequest request) {
        ClientUtils.getRemoteIP(request);
        return "index";
    }

    @GetMapping("/stock")
    public String openInfo(HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        return "/stock_info";
    }

    @GetMapping("/search")
    public String search(HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        return "/search";
    }

//    @GetMapping("/test")
//    public String openTest(){
//        return "/test";
//    }
}
