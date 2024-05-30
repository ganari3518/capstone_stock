package com.syu.capstone_stock.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockController {
    @GetMapping("/")
    public String openIndex() {
        return "index";
    }

    @GetMapping("/stock")
    public String openInfo(){
        return "/stock_info";
    }

    @GetMapping("/search")
    public String search(){
        return "/search";
    }

//    @GetMapping("/test")
//    public String openTest(){
//        return "/test";
//    }
}
