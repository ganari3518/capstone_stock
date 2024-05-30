package com.syu.capstone_stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.syu.capstone_stock.domain.Post;
import com.syu.capstone_stock.domain.PostIgnore;
import com.syu.capstone_stock.dto.PostRequestDto;
import com.syu.capstone_stock.repositry.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    @Transactional
    public Post save(final PostRequestDto postRequestDto){
        Post post = postRepository.save(postRequestDto.toEntity());
        if(post != null){
            return post;
        } else {
            throw new NullPointerException();
        }
    }

    @Transactional
    public Post update(final PostRequestDto postRequestDto){
        if(postRequestDto != null && postRequestDto.getId() != null){
            Optional<Post> post = postRepository.findById(postRequestDto.getId());
            if(!post.isEmpty()){
                post.get().setService(postRequestDto.getService());
                post.get().setApikey(postRequestDto.getApikey());
                post.get().setModifiedDate(LocalDateTime.now());
                return postRepository.save(post.get());
            } else {
                throw new NullPointerException();
            }
        } else {
            throw new NullPointerException();
        }
    }

    public Post deleteById(final Long id){
        if(id != null){
            Post post = findById(id);
            post.setDeleteYn(true);
            post.setModifiedDate(LocalDateTime.now());
            return postRepository.save(post);
        } else {
            throw new NullPointerException();
        }
    }

    public Post findById(final Long id){
        if(id != null){
            Optional<Post> post = postRepository.findById(id);
            Post p = post.get();
            return p;
        } else {
            throw new NullPointerException();
        }
    }

    public String getPostList(final String email){
        List<Post> posts = postRepository.findAllByDeleteYnAndEmailOrderByCreatedDateDesc(false, email);
        SimpleDateFormat s = new SimpleDateFormat("YYYY-MM-DD HH:mm");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()).setDateFormat(s);
        objectMapper.addMixInAnnotations(Post.class, PostIgnore.class);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        String jsonResult;
        try{
            jsonResult = objectMapper.writeValueAsString(posts);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return jsonResult;
    }
}
