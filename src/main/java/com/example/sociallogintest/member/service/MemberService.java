package com.example.sociallogintest.member.service;

import com.example.sociallogintest.member.dto.MemberDTO;
import com.example.sociallogintest.member.entity.Member;

import java.util.List;

public interface MemberService {

    // 회원가입
    Member registerMember(MemberDTO memberDTO);

    // 회원 정보 수정



    // 회원 삭제
    void deleteMember(String id);

    // 회원 검색


    // 회원 정보 반환(마이페이지)
    MemberDTO memberInfo(String id);

    // 회원 목록 가져오기
    List<MemberDTO> getList();

    // 엔티티를 DTO로 변환
    default MemberDTO entityToDto(Member entity) {
        return MemberDTO.builder()
//                .id(entity.getId())
                .password(entity.getPassword())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .role(entity.getRole())
                .score(entity.getScore())
                .profilePhotoUrl(entity.getProfilePhotoUrl())
                .createDate(entity.getCreateDate())
                .updateDate(entity.getUpdateDate())
                .build();
    }




//    List<Member> getAllMembers();
//
//    Optional<Member> getMemberById(String id);
//
//    Member saveMember(Member member);
//
//    void deleteMember(String id);
//
//    Member updateMember(String id, Member updatedMember);
//
//    boolean login(String id, String password);
}

