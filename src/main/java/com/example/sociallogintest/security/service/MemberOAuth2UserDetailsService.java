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
        String phone = "구글소셜로그인"; // 기본 전화번호
        String id = ""; // 사용자 입력 ID
        String name = ""; // 사용자 이름
        String password = "1111"; // 비밀번호 초기값

        // Google 사용자 정보에서 phone을 가져오지 않으므로, 기본값 사용
        // 실제로 전화번호를 가져오고 싶다면 추가적인 API 요청이 필요합니다.
        if (clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name"); // 이름 가져오기
        }

        log.info("User attributesZZZ: " + oAuth2User.getAttributes());
        log.info("nameZZZ: " + name);
        log.info("emailZZZ: " + email);
        log.info("phoneZZZ: " + phone);

//        Member member = saveSocialMember(email, phone);
//
//        return oAuth2User;

        // 회원가입처리
        Member member = saveSocialMember(email, phone, id, password, name);

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

    public Member saveSocialMember(String email, String phone, String id, String password, String name) {

        // 기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<Member> result = repository.findByEmail(email, true);

        if(result.isPresent()) {
            Member existingMember = result.get();
            log.info("Found member: " + existingMember);
            return existingMember;
        }

        // 없다면 회원 추가 패스워드는 1111 이름은 그냥 이메일 주소로
        Member member = Member.builder()
                .id(id)
                .email(email)
                .name(name)
//                .password(passwordEncoder.encode("1111")) // 임시비밀번호
                .password(passwordEncoder.encode(password))
                .phone(phone) // 전화번호 추가
                .fromSocial(true)
                .role("USER")
                .build();

        member.addMemberRole(MemberRole.USER); // 임시 권한

        log.info("User roles: " + member.getRole()); // 성민이꺼라 role은 null.
        log.info("Assigned roles: " + member.getRoleSet());

        repository.save(member);

        return member;
    }

}
