package com.example.sociallogintest.security.handler;

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
// 로그인 성공이후 처리를 위한 Handler

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private PasswordEncoder passwordEncoder;

    public LoginSuccessHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("-----------------------");
        log.info("onAuthenticationSuccess");

        AuthMemberDTO authMember = (AuthMemberDTO) authentication.getPrincipal(); // getPrincipal : 인증된 사용자 정보 가져오기

        boolean fromSocial = authMember.isFromSocial();

        log.info("Need Modify Member?" + fromSocial);

        boolean passwordResult = passwordEncoder.matches("1111", authMember.getPassword()); // 패스워드가 1111이라면 회원수정 페이지로 이동

        if (fromSocial && passwordResult) {
            // 비밀번호가 1111이면 수정 페이지로 리다이렉트
            redirectStrategy.sendRedirect(request, response, "/member/modify?from=social");
        } else {
            // 비밀번호가 1111이 아니면 /sample/member로 리다이렉트
            redirectStrategy.sendRedirect(request, response, "/sample/member");
        }

    }
}
