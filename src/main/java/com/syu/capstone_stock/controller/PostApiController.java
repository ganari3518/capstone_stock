package com.syu.capstone_stock.controller;

import com.syu.capstone_stock.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PostApiController {
    private final PostService postService;

    @PostMapping("/list/key")
    @ResponseBody
    public String findAll(HttpServletRequest request){
        String s = postService.getPostList(request.getParameter("email"));
        return s;
    }
}
