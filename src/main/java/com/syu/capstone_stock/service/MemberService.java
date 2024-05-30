package com.syu.capstone_stock.service;

import com.syu.capstone_stock.domain.Member;
import com.syu.capstone_stock.dto.MemberRequestDto;
import com.syu.capstone_stock.repositry.MailRepository;
import com.syu.capstone_stock.repositry.MemberRepository;
import com.syu.capstone_stock.util.EncryptionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MailRepository mailRepository;
    private Member m;

    @Transactional
    public Long saveMember(final MemberRequestDto params){
        if(mailRepository.findByEmailAndMailauth(params.getEmail(), true) != null) {
            return memberRepository.save(params.toEntity()).getId();
        } else {
            return null;
        }
    }

    public Member findMemberByLoginId(final String loginId){
        return memberRepository.findByDeleteYnAndLoginId(false, loginId);
    }

    private Member findMemberById(final Long id){
        return memberRepository.findById(id).get();
    }

    @Transactional
    public Long updateMember(final MemberRequestDto params){
        if(params != null && params.getId() != null){
            m = findMemberById(params.getId());
            m.setName(params.getName());
            m.setGender(params.getGender());
            m.setRegNo(EncryptionUtil.aesEncode(params.getRegNo()));
            if(params.getPassword().isEmpty() || params.getPassword().isBlank() || params.getPassword() == null || params.getPassword() == ""){
                m.setPassword(m.getPassword());
            } else {
                m.setPassword(EncryptionUtil.sha256Encode(params.getPassword()));
            }
            m.setModifiedDate(LocalDateTime.now());
            return memberRepository.save(m).getId();
        } else {
            throw new NullPointerException();
        }
    }

    @Transactional
    public Long deleteMemberById(final Long id){
        if(id != null){
            m = findMemberById(id);
            m.setDeleteYn(true);
            m.setModifiedDate(LocalDateTime.now());
            return memberRepository.save(m).getId();
        } else {
            throw new NullPointerException();
        }
    }

    public Member login(final String loginId, final String password){
        String enc_password = EncryptionUtil.sha256Encode(password);
        m = findMemberByLoginId(loginId);
        if(m == null || !m.getPassword().equals(enc_password)){
            return null;
        }

        m.setPassword(null);
        return m;
    }

    public int countMemberByLoginId(final String loginId){
        return memberRepository.countByLoginId(loginId);
    }
}
