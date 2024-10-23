package com.example.sociallogintest.config;

import com.example.sociallogintest.security.handler.LoginSuccessHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("---------------filterChain---------------");

        http.authorizeHttpRequests(auth -> {
                    // 모든 사용자가 접근할 수 있는 경로
                    auth.requestMatchers("/sample/all").permitAll()
                            // 모든 사용자가 접근할 수 있는 경로
                            .requestMatchers("/sample/member").permitAll()
//                            .requestMatchers("/sample/member").hasRole("USER")
                            // OAuth2 관련 요청은 모두 허용
                            .requestMatchers("/oauth2/**").permitAll()
//                            .requestMatchers("/oauth2/**").hasRole("USER")
                            // 나머지 요청은 인증 필요
                            .anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .permitAll()
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/sample/member");
                        })

                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        http.csrf(csrf -> csrf.disable());

        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/sample/member") // 로그인 성공 후 리다이렉션URL
                .failureUrl("/login?error")
                .successHandler(successHandler()) // 로그인 성공시 핸들러
        );

/*        http.oauth2Login(oauth2 -> {

        });*/

//        http.oauth2Login()
//                .successHandler(new CustomAuthenticationSuccessHandler());

        return http.build();
    }

    @Bean
    public LoginSuccessHandler successHandler(){
        return new LoginSuccessHandler(passwordEncoder());
    }
}