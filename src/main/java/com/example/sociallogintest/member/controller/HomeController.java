package com.example.sociallogintest.member.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
public class HomeController {

    @GetMapping("/login")
    public String login() {
        return "/login"; //
    }

}
