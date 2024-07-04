package com.syu.capstone_stock.controller;

import com.syu.capstone_stock.service.PostService;
import com.syu.capstone_stock.util.ClientUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3000, allowCredentials = "true")
public class PostApiController {
    private final PostService postService;

    @PostMapping("/list/key")
    @ResponseBody
    public String findAll(HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        String s = postService.getPostList(request.getParameter("email"));
        return s;
    }
}
