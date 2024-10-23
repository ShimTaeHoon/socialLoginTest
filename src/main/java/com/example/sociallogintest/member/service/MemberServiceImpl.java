package com.example.sociallogintest.member.service;

import com.example.sociallogintest.member.dto.MemberDTO;
import com.example.sociallogintest.member.entity.Member;
import com.example.sociallogintest.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    @Override
    public Member registerMember(MemberDTO memberDTO) {

//        if (memberRepository.existsById(memberDTO.getId())) {
//            throw new RuntimeException("이미 사용 중인 ID입니다.");
//        } else if (memberRepository.existsById(memberDTO.getEmail())){
//            throw new RuntimeException("이미 사용중인 이메일입니다.");
//        }

        Member member = Member.builder()
//                .id(memberDTO.getId())
                .password(memberDTO.getPassword())
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .phone(memberDTO.getPhone())
                .role("ROLE_USER")
                .score(0.0) // 점수는 항상 0으로 초기화
                .profilePhotoUrl("")
                .build();

        return memberRepository.save(member);
    }

    // 회원 삭제
    @Override
    public void deleteMember(String id) {
        Optional<Member> optional = memberRepository.findById(id);
        if(optional.isPresent()) {
            memberRepository.deleteById(id);
        }
    }

    // 회원 정보 반환(마이페이지)
    @Override
    public MemberDTO memberInfo(String id) {
        Optional<Member> optional = memberRepository.selectById(id);
        if (optional.isPresent()) {
            Member member = optional.get();
            return entityToDto(member);
        }
        return null;
    }


    // 회원 목록 반환(어드민 페이지)
    @Override
    public List<MemberDTO> getList() {
        List<Member> result = memberRepository.findAll();
        return result.stream().map(this::entityToDto).collect(Collectors.toList());
    }



//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Override
//    public List<Member> getAllMembers() {
//        return memberRepository.findAll();
//    }
//
//    @Override
//    public Optional<Member> getMemberById(String id) {
//        return memberRepository.findById(id);
//    }
//
//    @Override
//    public Member saveMember(Member member) {
//        return memberRepository.save(member);
//    }
//
//    @Override
//    public void deleteMember(String id) {
//        memberRepository.deleteById(id);
//    }
//
//    @Override
//    public Member updateMember(String id, Member updatedMember) {
//        Optional<Member> memberOptional = memberRepository.findById(id);
//        if (memberOptional.isPresent()) {
//            Member existingMember = memberOptional.get();
//            // 필드 업데이트 (예: 이름, 이메일 등)
//            existingMember.setName(updatedMember.getName());
//            existingMember.setEmail(updatedMember.getEmail());
//            existingMember.setPhone(updatedMember.getPhone());
//            existingMember.setRole(updatedMember.getRole());
//            existingMember.setScore(updatedMember.getScore());
//            return memberRepository.save(existingMember);
//        } else {
//            throw new RuntimeException("해당 회원을 찾을 수 없습니다.");
//        }
//    }
//
//    @Override
//    public boolean login(String id, String password) {
//        Member member = memberRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
//
//        // 비밀번호 비교 (암호화 없이)
//        return member.getPassword().equals(password);
//    }
}