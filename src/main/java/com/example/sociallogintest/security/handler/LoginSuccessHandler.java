package com.example.sociallogintest.security.handler;

import com.example.sociallogintest.member.entity.Member;
import com.example.sociallogintest.member.repository.MemberRepository;
import com.example.sociallogintest.security.dto.AuthMemberDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private PasswordEncoder passwordEncoder;
    private MemberRepository memberRepository;

    public LoginSuccessHandler(PasswordEncoder passwordEncoder, MemberRepository memberRepository) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("onAuthenticationSuccess");
        AuthMemberDTO authMember = (AuthMemberDTO) authentication.getPrincipal();
        boolean fromSocial = authMember.isFromSocial();

        log.info("Need Modify Member? " + fromSocial);
        Member member = memberRepository.findByEmail(authMember.getEmail(), fromSocial).orElse(null);

        // 회원가입 페이지
        // 해당 이메일 조회 회원의 정보가 없다면 회원가입페이지 이동
        if (member == null && fromSocial) {
            redirectStrategy.sendRedirect(request, response, "/member/socialRegister?email=" + authMember.getEmail() + "&name=" + authMember.getName());
        } else {
            // 비밀번호 체크 로직은 실제 비밀번호 사용으로 변경
            boolean passwordResult = passwordEncoder.matches("작성한 비밀번호", member.getPassword());
            if (passwordResult) {
                redirectStrategy.sendRedirect(request, response, "/member/modify");
            } else {
                redirectStrategy.sendRedirect(request, response, "/sample/member");
            }
        }
    }
}
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        log.info("-----------------------");
//        log.info("onAuthenticationSuccess");
//
//        AuthMemberDTO authMember = (AuthMemberDTO) authentication.getPrincipal();
//        boolean fromSocial = authMember.isFromSocial();
//
//        log.info("Need Modify Member? " + fromSocial);
//        log.info("AuthMemberDTO: " + authMember); // 추가된 로그
//
//        Member member = memberRepository.findByEmail(authMember.getEmail(), fromSocial).orElse(null);
//        log.info("Member: " + member); // 추가된 로그
//
//        if (member == null) {
//            redirectStrategy.sendRedirect(request, response, "/member/socialRegister?email=" + authMember.getEmail() + "&name=" + authMember.getName());
//        } else if (fromSocial) {
//            redirectStrategy.sendRedirect(request, response, "/member/socialRegister?email=" + authMember.getEmail() + "&name=" + authMember.getName());
//        } else {
//            boolean passwordResult = passwordEncoder.matches("1111", member.getPassword());
//            if (passwordResult) {
//                redirectStrategy.sendRedirect(request, response, "/member/modify");
//            } else {
//                redirectStrategy.sendRedirect(request, response, "/sample/member");
//            }
//        }
//    }

