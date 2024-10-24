package com.example.sociallogintest.member.controller;

import com.example.sociallogintest.security.service.MemberOAuth2UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member")
public class SocialSignupController {

    private final MemberOAuth2UserDetailsService memberOAuth2UserDetailsService;

    public SocialSignupController(MemberOAuth2UserDetailsService memberOAuth2UserDetailsService) {
        this.memberOAuth2UserDetailsService = memberOAuth2UserDetailsService;
    }

    @GetMapping("/socialRegister")
    public String showSignupForm(@RequestParam String email, @RequestParam String name, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        return "member/socialRegister"; // socialRegister.html로 이동
    }

    @PostMapping("/socialRegister")
    public String handleSignup(@RequestParam String id,
                               @RequestParam String password,
                               @RequestParam String phone,
                               @RequestParam String email,
                               @RequestParam String name) {
        memberOAuth2UserDetailsService.saveSocialMember(email, phone, id, password, name);
        return "redirect:/success"; // 성공 페이지로 리다이렉션
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "member/success"; // success.html로 이동
    }

}
