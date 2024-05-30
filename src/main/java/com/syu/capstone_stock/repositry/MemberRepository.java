package com.syu.capstone_stock.repositry;

import com.syu.capstone_stock.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByDeleteYnAndLoginId(Boolean b, String loginId);
    Integer countByLoginId(String loginId);
    Member findByEmailAndMailauth(String email, Boolean b);
}