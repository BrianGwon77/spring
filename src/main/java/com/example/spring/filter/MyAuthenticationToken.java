package com.example.passwordinitializer.filter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MyAuthenticationToken extends AbstractAuthenticationToken {

    /** 인증코드 **/
    private String employeeNo;
    private String credentials;

    public MyAuthenticationToken(String employeeNo, String authenticationCode, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.employeeNo = employeeNo;
        this.credentials = authenticationCode;
    }

    public MyAuthenticationToken(String employeeNo, String authenticationCode) {
        super(null);
        this.employeeNo = employeeNo;
        this.credentials = authenticationCode;
    }

    public MyAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return employeeNo;
    }

}
