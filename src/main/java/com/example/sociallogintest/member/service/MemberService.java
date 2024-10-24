package com.example.sociallogintest.member.service;

import com.example.sociallogintest.member.dto.MemberDTO;
import com.example.sociallogintest.member.entity.Member;

import java.util.List;

public interface MemberService {

    // 회원가입
    Member registerMember(Member member);

    // 회원 정보 수정



    // 회원 삭제
    void deleteMember(String id);

    // 회원 검색


    // 회원 정보 반환(마이페이지)
    MemberDTO memberInfo(String id);

    // 회원 목록 가져오기
    List<MemberDTO> getList();

    // DTO에서 엔티티로 변환
    default Member dtoToEntity(MemberDTO memberDTO) {
        return Member.builder()
                .id(memberDTO.getId())
                .password(memberDTO.getPassword())
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .phone(memberDTO.getPhone())
                .profilePhotoUrl(memberDTO.getProfilePhotoUrl())
                .role(memberDTO.getRole())
                .score(memberDTO.getScore())
                .build();
    }

    // 엔티티에서 DTO로 변환
    default MemberDTO entityToDto(Member entity) {
        return MemberDTO.builder()
                .id(entity.getId())
                .password(entity.getPassword())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .role(entity.getRole())
                .profilePhotoUrl(entity.getProfilePhotoUrl())
                .score(entity.getScore())
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

