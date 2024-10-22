package com.example.sociallogintest.security.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MemberOAuth2UserDetailsService extends DefaultOAuth2UserService {

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("----------------------------");
        log.info("userRequest: " + userRequest);

        return super.loadUser(userRequest);

    }

}
