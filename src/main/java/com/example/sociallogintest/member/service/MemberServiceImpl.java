package com.example.sociallogintest.member.service;

import com.example.sociallogintest.member.dto.MemberDTO;
import com.example.sociallogintest.member.entity.Member;
import com.example.sociallogintest.member.entity.MemberRole;
import com.example.sociallogintest.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입

    // 회원가입
    @Override
    public Member registerMember(Member member) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword); // 암호화된 비밀번호로 설정

        // 기본 역할 추가
        member.setRole(String.valueOf(MemberRole.USER));

        // 엔티티 저장
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
//        Optional<Member> optional = memberRepository.selectById(id);
//        if (optional.isPresent()) {
//            Member member = optional.get();
//            return entityToDto(member);
//        }
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