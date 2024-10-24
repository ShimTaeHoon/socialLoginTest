package com.example.sociallogintest;

import com.example.sociallogintest.member.entity.Member;
import com.example.sociallogintest.member.entity.MemberRole;
import com.example.sociallogintest.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberTests {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummyMembers() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .id(i)
                    .email("user" + i + "@example.com")
                    .name("사용자" + i)
                    .password(passwordEncoder.encode("1111")) // 비밀번호 암호화
                    .phone("010-1234-56" + (i < 10 ? "0" + i : i)) // 전화번호 포맷
                    //소셜 로그인 회원 (fromSocial = true)
                    .fromSocial(false) // 일반 회원
                    .build();

            // 기본 역할을 USER로 설정
            member.addMemberRole(MemberRole.USER);

            // 등급 설정
            if (i > 80) {
                member.addMemberRole(MemberRole.MANAGER);
            }

            if (i > 90) {
                member.addMemberRole(MemberRole.ADMIN);
            }

            // 멤버 저장
            repository.save(member);
        });
    }

    @Test
    public void insertDummyOne() {
        Member member = Member.builder()
                .id(777)
                .email("user777@example.com")
                .name("사용자777")
                .password("1111") // 비밀번호 암호화
                .phone("010-1234-5678") // 전화번호 포맷
                //소셜 로그인 회원 (fromSocial = true)
                .fromSocial(false) // 일반 회원
                .build();

        member.addMemberRole(MemberRole.USER);

        repository.save(member);
    }

    @Test
    public void testRead() {
        Optional<Member> result = repository.findByEmail("user10@example.com", false);

        if (result.isPresent()) {
            Member member = result.get();
            System.out.println(member.getName()); // 여기서 name을 출력해 확인
        } else {
            System.out.println("회원 정보를 찾을 수 없습니다.");
        }
    }
}