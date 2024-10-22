package com.example.sociallogintest.member.controller;

import com.example.sociallogintest.security.dto.AuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {

    @GetMapping("/all")
    public void exAll() {
        log.info("exAll..........");
    }

    // @AuthenticationPrincipal으로 로그인된 사용자 정보 확인
    @GetMapping("/member")
    public String exMember(@AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info("exMember...........");
        log.info("--------------------");
        log.info(authMember);

        return "/sample/member";
    }

    @GetMapping("/admin")
    public void exAdmin() {
        log.info("exMember...........");
    }


}
