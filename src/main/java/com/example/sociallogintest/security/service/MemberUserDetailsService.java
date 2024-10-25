    package com.example.sociallogintest.security.service;

    import com.example.sociallogintest.member.entity.Member;
    import com.example.sociallogintest.member.repository.MemberRepository;
    import com.example.sociallogintest.security.dto.AuthMemberDTO;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.log4j.Log4j2;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    @Log4j2
    public class MemberUserDetailsService implements UserDetailsService{

        private final MemberRepository memberRepository;
        private final PasswordEncoder passwordEncoder; // PasswordEncoder 사용

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            // 사용자 존재 여부 확인
            // TODO: 이메일을 받아서 해당회원이 있는지 조회 -> 아이디를 받아서 해당회원이 있는지 조회
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

            log.info("Loaded member: {}", member);

            return new AuthMemberDTO(
                    member.getEmail(),

                    member.getPassword(),
                    member.isFromSocial(),
                    member.getRoleSet().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                            .collect(Collectors.toSet())
            );
        }

        public boolean checkPassword(String rawPassword, String encodedPassword) {
            return passwordEncoder.matches(rawPassword, encodedPassword);
        }
//
//        @Override
//        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//            log.info("UserDetailsService loadUserByUsername");
//
//            Optional<Member> result = memberRepository.findByEmail(username, false);
//
//            if(result.isPresent()) {
//                throw new UsernameNotFoundException("Check Email or Social");
//            }
//
//            Member member = result.get();
//
//            log.info("----------------------");
//            log.info(member);
//
//            AuthMemberDTO authMember = new AuthMemberDTO(
//                    member.getEmail(),
//                    member.getPassword(),
//                    member.isFromSocial(),
//                    member.getRoleSet().stream()
//                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet())
//            );
//
//            authMember.setName(member.getName());
//            authMember.setFromSocial(member.isFromSocial());
//
//            return authMember;
//
//        }
    }