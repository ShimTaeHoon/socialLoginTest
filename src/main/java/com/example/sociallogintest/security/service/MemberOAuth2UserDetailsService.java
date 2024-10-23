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
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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
        oAuth2User.getAttributes().forEach((k,v) -> {
            log.info("key: " + k + " value: " + v); // sub, picture, email, email_verified,... 출력
        });

        String email = null;
        String phone = "구글소셜로그인";
        // Google 사용자 정보에서 phone을 가져오지 않으므로, 기본값 사용
        // 실제로 전화번호를 가져오고 싶다면 추가적인 API 요청이 필요합니다.

        if(clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
        }

        log.info("email: " + email);
        log.info("phone: " + phone);

//        Member member = saveSocialMember(email, phone);
//
//        return oAuth2User;

        Member member = saveSocialMember(email, phone);

        AuthMemberDTO AuthMember = new AuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                true, // fromSocial
                member.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );
        AuthMember.setName(member.getName());

        return AuthMember;

    }

    private Member saveSocialMember(String email, String phone) {

        // 기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<Member> result = repository.findByEmail(email, true);

        if(result.isPresent()) {
            Member existingMember = result.get();
            log.info("Found member: " + existingMember);
            return existingMember;
        }

        // 없다면 회원 추가 패스워드는 1111 이름은 그냥 이메일 주소로
        Member member = Member.builder().email(email)
                .name(email)
                .password(passwordEncoder.encode("1111")) // 임시비밀번호
                .phone(phone) // 전화번호 추가
                .fromSocial(true)
                .build();

        member.addMemberRole(MemberRole.USER); // 임시 권한

        log.info("User roles: " + member.getRole()); // 성민이꺼라 role은 null.

        repository.save(member);

        return member;
    }

}
