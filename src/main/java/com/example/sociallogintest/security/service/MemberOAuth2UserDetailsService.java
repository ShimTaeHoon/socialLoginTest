package com.example.sociallogintest.security.service;

import com.example.sociallogintest.member.entity.Member;
import com.example.sociallogintest.member.entity.MemberRole;
import com.example.sociallogintest.member.repository.MemberRepository;
import com.example.sociallogintest.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final MemberRepository repository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("----------------------------");
        log.info("userRequest: " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("clientName: " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("=======================");
        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info("key: " + k + " value: " + v);
        });

        String email = null;
        String name = "";

        // Google 사용자 정보에서 email과 name 가져오기
        if (clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
        }

        log.info("User attributes: " + oAuth2User.getAttributes());
        log.info("name: " + name);
        log.info("email: " + email);



        // AuthMemberDTO 생성
        AuthMemberDTO authMember = new AuthMemberDTO(
                email,          // 이메일
                "1111",        // 임시 비밀번호 또는 실제 비밀번호
                true,          // 소셜 로그인 여부
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")), // 권한
                oAuth2User.getAttributes() // 소셜 사용자 속성
        );
        authMember.setName(name); // 사용자 이름 설정

        return authMember;
    }


    public Member saveSocialMember(String email, String name, String password, String phone) {
        // 기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<Member> result = repository.findByEmail(email, true);

        if (result.isPresent()) {
            Member existingMember = result.get();
            log.info("Found member: " + existingMember);
            return existingMember;
        }

        // 없다면 회원 추가
        Member member = Member.builder()
                .id(email) // 이메일을 ID로 사용
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(password)) // 비밀번호 암호화
                .phone(phone) // 전화번호 추가
                .fromSocial(true)
                .role("USER")
                .build();

        member.addMemberRole(MemberRole.USER); // 역할 추가

        repository.save(member); // DB에 저장

        return member;
    }

}
