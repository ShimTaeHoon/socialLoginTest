package com.example.sociallogintest.member.controller;

import com.example.sociallogintest.member.entity.Member;
import com.example.sociallogintest.member.entity.MemberRole;
import com.example.sociallogintest.member.repository.MemberRepository;
import com.example.sociallogintest.security.dto.AuthMemberDTO;
import com.example.sociallogintest.security.service.MemberOAuth2UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberOAuth2UserDetailsService memberOAuth2UserDetailsService;
    Member member;

    // 소셜 로그인 후 사용자 입력 페이지 표시
    @GetMapping("/member/socialRegister")
    public String showRegisterPage(@RequestParam String email, @RequestParam String name, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        return "member/socialRegister"; // 사용자 입력 폼 페이지
    }

    // 폼에 사용자 입력을 받음 (EMAIL과 NAME은 구글에서 제공받음)
    @PostMapping("/member/socialRegister")
    public String registerMember(@RequestParam String id,
                                 @RequestParam String password,
                                 @RequestParam String phone,
                                 @RequestParam String email,
                                 @RequestParam String name) {

        log.info("비밀번호 암호화 방식: " + passwordEncoder.getClass().getSimpleName());

        Member member = new Member();
        member.setId(id);
        member.setEmail(email); // Google에서 제공된 이메일
        member.setName(name);   // Google에서 제공된 이름
        member.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
        member.setPhone(phone);
        member.setRole("ROLE_USER");
        member.setFromSocial(true); // 소셜 회원 여부 설정
        member.addMemberRole(MemberRole.USER); // 기본 권한 부여

        // DB에 저장
        memberRepository.save(member);

        log.info("Registered new member: " + member);
        log.info("회원가입 완료 후 로그인 시도: " + email);

        // 회원가입 완료 후 리다이렉트
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }

    // 수정 페이지 (일반 및 소셜 회원 모두 처리)
    @GetMapping("/member/modify")
    public String showModifyPage(Authentication authentication, Model model) {
        Object principal = authentication.getPrincipal();
        AuthMemberDTO authMember = (AuthMemberDTO) principal;
        boolean fromSocial = authMember.isFromSocial();

        // DB에서 회원 정보 조회
        Member member = memberRepository.findByEmail(authMember.getEmail(), fromSocial)
                .orElse(null);

        if (member == null) {
            // 회원 정보가 없으면 소셜 회원가입 페이지로 리다이렉트
            return "redirect:/member/socialRegister?email=" + authMember.getEmail() + "&name=" + authMember.getName();
        }

        // 회원 정보가 있으면 수정 페이지로 이동
        model.addAttribute("member", member);
        model.addAttribute("isSocialMember", fromSocial);

        return "member/modify"; // 수정 페이지로 이동
    }

    @PostMapping("/member/update")
    public String updateMember(Authentication authentication, String name, String password) {
        Object principal = authentication.getPrincipal();
        AuthMemberDTO authMember = (AuthMemberDTO) principal;
        boolean fromSocial = authMember.isFromSocial();

        // DB에서 회원 정보 조회
        Member member = memberRepository.findByEmail(authMember.getEmail(), fromSocial)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        // 이름 수정
        member.setName(name);

        // 비밀번호 수정
        if (password != null && !password.isEmpty()) {
            member.setPassword(passwordEncoder.encode(password));
        }

        // 변경된 회원 정보 저장
        memberRepository.save(member);

        log.info("Updated member: " + member);

        // 수정 완료 후 리다이렉트
        return "redirect:/sample/member"; // 수정 완료 후 리다이렉트
    }
}
