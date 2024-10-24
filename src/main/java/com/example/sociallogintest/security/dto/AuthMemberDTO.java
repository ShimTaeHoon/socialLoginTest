package com.example.sociallogintest.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


//이 DTO는 Spring Security에서 사용자 인증 및 권한 부여에 사용됩니다. 즉, 인증 정보를 담고 있는 클래스로,
//주로 로그인 시 사용되며, UserDetails와 OAuth2User 인터페이스를 구현하고 있습니다.
//이 DTO는 인증 관련된 속성(예: 권한, 소셜 로그인 여부 등)을 추가로 포함하고 있습니다.
//사용자 인증 시 필요한 정보와 기능을 제공
@Getter
@Setter
@Log4j2
@ToString
public class AuthMemberDTO extends User implements OAuth2User {

    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    private Map<String, Object> attr;

    public AuthMemberDTO(
        String username,
        String password,
        boolean fromSocial,
        Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr) {

        this(username, password, fromSocial, authorities);
        this.attr = attr;

    }

    public AuthMemberDTO(
            String username,
            String password,
            boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities){

        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.fromSocial = fromSocial;

    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }

}
