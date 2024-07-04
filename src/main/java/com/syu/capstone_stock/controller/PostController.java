package com.syu.capstone_stock.controller;

import com.syu.capstone_stock.domain.Post;
import com.syu.capstone_stock.dto.PostRequestDto;
import com.syu.capstone_stock.service.PostService;
import com.syu.capstone_stock.util.ClientUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3000, allowCredentials = "true")
public class PostController {
    private final PostService postService;
    @GetMapping("/key/write.do")
    public String openPostWrite(@RequestParam(value = "id", required = false) final Long id, Model model, HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        if(id != null){
            Post post = postService.findById(id);
            model.addAttribute("post",post);
        }
        return "/key/write";
    }

    @PostMapping("/key/save.do")
    public String savePost(final PostRequestDto params, HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        postService.save(params);
        return "redirect:/setting/list.do";
    }

    @PostMapping("/key/update.do")
    public String updatePost(PostRequestDto params, Model model, HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        postService.update(params);
        return "redirect:/setting/list.do";
    }

    @PostMapping("/key/delete.do")
    public String deletePost(@RequestParam final Long id, HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        postService.deleteById(id);
        return "redirect:/setting/list.do";
    }

    @GetMapping("/key/view.do")
    public String openPostView(@RequestParam final Long id, Model model, HttpServletRequest request){
        ClientUtils.getRemoteIP(request);
        Post post = postService.findById(id);
        model.addAttribute("post",post);
        return "/key/view";
    }
}
