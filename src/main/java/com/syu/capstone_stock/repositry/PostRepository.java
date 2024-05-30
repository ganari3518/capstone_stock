package com.syu.capstone_stock.repositry;

import com.syu.capstone_stock.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByDeleteYnAndEmailOrderByCreatedDateDesc(Boolean b, String email);
}
